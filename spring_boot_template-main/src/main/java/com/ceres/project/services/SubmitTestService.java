package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.*;
import com.ceres.project.repositories.*;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitTestService extends BaseWebActionsService {

    private final AnswerOptionRepository answerOptionRepository;
    private final TestSubmissionRepository testSubmissionRepository;
    private final UserTopicProgressRepository userTopicProgressRepository;
    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;

    // Submit Test
    public OperationReturnObject submitTest(JSONObject request) {
        requiresAuth();

        SystemUserModel user = authenticatedUser();
        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Integer testId = data.getIntValue("testId");
            JSONArray submittedAnswers = data.getJSONArray("answers");

            // Fetch all actual questions for the test
            List<Question> allQuestions = questionRepository.findByTestId(testId);
            int totalQuestions = allQuestions.size();
            int correctAnswers = 0;

            // Map submitted answers by questionId for fast lookup
            Map<Integer, Integer> submittedAnswerMap = new HashMap<>();
            for (int i = 0; i < submittedAnswers.size(); i++) {
                JSONObject answer = submittedAnswers.getJSONObject(i);
                int questionId = answer.getIntValue("questionId");
                Integer selectedAnswerId = null;
                try {
                    selectedAnswerId = answer.getIntValue("selectedAnswerId");
                    submittedAnswerMap.put(questionId, selectedAnswerId);
                } catch (Exception ignored) {
                    // Leave unanswered if invalid or missing
                }
            }

            // Evaluate answers, count unanswered as incorrect
            for (Question question : allQuestions) {
                int qId = question.getId().intValue();
                Integer selectedAnswerId = submittedAnswerMap.get(qId);

                if (selectedAnswerId == null) continue; // Unanswered, count as wrong

                AnswerOption correct = answerOptionRepository.findFirstByQuestionIdAndIsCorrectTrue(qId);
                if (correct != null && correct.getId().intValue() == selectedAnswerId.intValue()) {
                    correctAnswers++;
                }
            }

            // Calculate score correctly
            double percentageScore = ((double) correctAnswers / totalQuestions) * 100.0;

            // Save or update submission
            TestSubmission submission = testSubmissionRepository.findByUserIdAndTestId(user.getServpaceId(), testId);
            if (submission == null) {
                submission = new TestSubmission();
                submission.setUserId(user.getServpaceId());
                submission.setTestId(testId);
            }

            submission.setScore(percentageScore);
            submission.setSubmittedAt(LocalDateTime.now());
            testSubmissionRepository.save(submission);

            // Update topic progress
            Integer topicId = testRepository.findTopicIdByTestId(testId);
            updateTopicProgress(user.getServpaceId(), topicId, percentageScore);

            // Prepare response
            JSONObject result = new JSONObject();
            result.put("score", percentageScore);
            result.put("correctAnswers", correctAnswers);
            result.put("totalQuestions", totalQuestions);

            returnObj.setReturnObject(result);
            returnObj.setReturnCodeAndReturnMessage(200, "Test submitted successfully. Score: " + percentageScore + "%");
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Internal Server Error: " + e.getMessage());
        }

        return returnObj;
    }


    public OperationReturnObject updateTopicProgress(String userId, Integer topicId, double score) {
        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            boolean passed = score > 50.0;

            UserTopicProgress progress = userTopicProgressRepository
                    .findByUserIdAndCourseTopicId(userId, topicId)
                    .orElseGet(() -> {
                        UserTopicProgress newProgress = new UserTopicProgress();
                        newProgress.setUserId(userId);
                        newProgress.setCourseTopicId(topicId);
                        return newProgress;
                    });

            progress.setProgressPercentage(score);
            progress.setCompleted(passed);

            if (passed) {
                progress.setCompletedAt(LocalDateTime.now());
            } else {
                progress.setCompletedAt(null);
            }
            userTopicProgressRepository.save(progress);
            returnObj.setReturnObject(progress);
            returnObj.setReturnCodeAndReturnMessage(200, passed ?
                    "Topic completed successfully." : "Topic progress updated. Test not passed.");

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Internal Server Error");
        }

        return returnObj;
    }

    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "submitTest" -> submitTest(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
