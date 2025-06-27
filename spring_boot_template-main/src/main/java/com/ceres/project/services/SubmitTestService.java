package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.AnswerOption;
import com.ceres.project.models.database.SystemUserModel;
import com.ceres.project.models.database.TestSubmission;
import com.ceres.project.models.database.UserTopicProgress;
import com.ceres.project.repositories.AnswerOptionRepository;
import com.ceres.project.repositories.TestRepository;
import com.ceres.project.repositories.TestSubmissionRepository;
import com.ceres.project.repositories.UserTopicProgressRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubmitTestService extends BaseWebActionsService {

    private final AnswerOptionRepository answerOptionRepository;
    private final TestSubmissionRepository testSubmissionRepository;
    private final UserTopicProgressRepository userTopicProgressRepository;
    private final TestRepository testRepository;

    // Submit Test
    public OperationReturnObject submitTest(JSONObject request) {

        // Ensure the user is authenticated before proceeding
        requiresAuth();

        SystemUserModel user = authenticatedUser();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            // Validate the required "data" field exists in the JSON payload
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            // Extract necessary fields from request data
            Integer testId = data.getIntValue("testId");
            JSONArray answers = data.getJSONArray("answers");

            // Track total and correct answers
            int totalQuestions = answers.size();
            int correctAnswers = 0;

            // Evaluate each submitted answer
            for (int i = 0; i < totalQuestions; i++) {
                JSONObject answer = answers.getJSONObject(i);
                int questionId = answer.getIntValue("questionId");

                Integer selectedAnswerId = null;
                try {
                    selectedAnswerId = answer.getIntValue("selectedAnswerId");
                } catch (Exception ignored) {
                    // If answer is missing or invalid, skip (counted as unanswered/incorrect)
                }

                if (selectedAnswerId == null) {
                    continue;
                }

                // Retrieve the correct answer option for the given question
                AnswerOption correct = answerOptionRepository.findFirstByQuestionIdAndIsCorrectTrue(questionId);

                // Compare selected answer to correct answer
                if (correct != null && correct.getId().intValue() == selectedAnswerId.intValue()) {
                    correctAnswers++;
                }
            }

            // Compute score as a percentage (0–100)
            double percentageScore = ((double) correctAnswers / totalQuestions) * 100.0;

            // Check if the user has already submitted this test before
            TestSubmission submission = testSubmissionRepository.findByUserIdAndTestId(user.getServpaceId(), testId);
            if (submission == null) {
                // First-time submission for this test
                submission = new TestSubmission();
                submission.setUserId(user.getServpaceId());
                submission.setTestId(testId);
            }

            // Set or update score and submission timestamp
            submission.setScore(percentageScore);
            submission.setSubmittedAt(LocalDateTime.now());

            // Save submission (create or update)
            testSubmissionRepository.save(submission);

            // 🟩 Fetch topicId for this test (you must implement this logic appropriately)
            Integer topicId = testRepository.findTopicIdByTestId(testId); // You must define this method

            // 🟩 Update topic progress if score > 50%
            //if (percentageScore >= 50.0) {
                updateTopicProgress(user.getServpaceId(), topicId, percentageScore);
            //}

            // Prepare response object
            JSONObject result = new JSONObject();
            result.put("score", percentageScore);
            result.put("correctAnswers", correctAnswers);
            result.put("totalQuestions", totalQuestions);

            // Return success result
            returnObj.setReturnObject(result);
            returnObj.setReturnCodeAndReturnMessage(200, "Test submitted successfully. Score: " + percentageScore + "%");

        } catch (Exception e) {
            // Handle unexpected errors
            returnObj.setReturnCodeAndReturnMessage(500, "Internal Server Error");
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
