package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.AnswerOption;
import com.ceres.project.models.database.Question;
import com.ceres.project.models.database.Test;
import com.ceres.project.repositories.AnswerOptionRepository;
import com.ceres.project.repositories.QuestionRepository;
import com.ceres.project.repositories.TestRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class TestService extends BaseWebActionsService {

    private final TestRepository testRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;

    // Create Test - Only one test per topic
    public OperationReturnObject createTopicTest(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long topicId = data.getLong("topicId");
            String title = data.getString("title");

            // Check if a test already exists for this topic
            Optional<Test> existingTest = testRepository.findByTopicId(topicId.intValue());
            if (existingTest.isPresent()) {
                returnObj.setReturnCodeAndReturnMessage(400, "A test already exists for this topic.");
                return returnObj;
            }

            // Create and save the Test entity
            Test test = new Test();
            test.setTitle(title);
            test.setTopicId(topicId.intValue());
            testRepository.save(test); // Save to get generated ID

            JSONArray questionsArray = data.getJSONArray("questions");

            for (int i = 0; i < questionsArray.size(); i++) {
                JSONObject qObj = questionsArray.getJSONObject(i);

                Question question = new Question();
                question.setContent(qObj.getString("content"));
                question.setTestId(test.getId().intValue());
                questionRepository.save(question); // Save to get generated ID

                JSONArray answersArray = qObj.getJSONArray("answerOptions");
                for (int j = 0; j < answersArray.size(); j++) {
                    JSONObject aObj = answersArray.getJSONObject(j);

                    AnswerOption answer = new AnswerOption();
                    answer.setAnswerText(aObj.getString("answerText"));
                    answer.setCorrect(aObj.getBoolean("isCorrect"));
                    answer.setQuestionId(question.getId().intValue());
                    answerOptionRepository.save(answer);
                }
            }

            returnObj.setReturnObject(test);
            returnObj.setReturnCodeAndReturnMessage(200, "Test and questions created successfully.");
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Failed to create test: " + e.getMessage());
        }

        return returnObj;
    }

    public  OperationReturnObject getTestAttempt(JSONObject request) {

        requiresAuth();
        OperationReturnObject returnObj = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            Integer topicId = data.getInteger("topicId");

            Test test = testRepository.findByTopicId(topicId).orElseThrow(() -> new RuntimeException("Test not found"));

            JSONObject response = new JSONObject();
            response.put("testId", test.getId());
            response.put("testTitle", test.getTitle());

            returnObj.setReturnCodeAndReturnMessage(200, "TestAttempt retrieved successfully");
            returnObj.setReturnObject(response);
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Error: " + e.getMessage());
        }

        return returnObj;
    }

    public OperationReturnObject getQuestionByTestId(JSONObject request) {

        requiresAuth();
        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            Integer testId = data.getInteger("testId");

            List<Question> allQuestions = questionRepository.findByTestId(testId);
            List<JSONObject> questionArray = new ArrayList<>();

            for (Question question : allQuestions) {
                JSONObject questionJson = new JSONObject();
                questionJson.put("questionId", question.getId());
                questionJson.put("questionTitle", question.getContent());

                // Get answer options for this question
                List<AnswerOption> answerOptions = answerOptionRepository.findByQuestionId(question.getId().intValue());

                List<JSONObject> optionsArray = new ArrayList<>();
                for (AnswerOption option : answerOptions) {
                    JSONObject optionJson = new JSONObject();
                    optionJson.put("id", option.getId());
                    optionJson.put("text", option.getAnswerText());
                    //optionJson.put("isCorrect", option.isCorrect());
                    optionsArray.add(optionJson);
                }

                questionJson.put("answerOptions", optionsArray);
                questionArray.add(questionJson);
            }

            returnObj.setReturnCodeAndReturnMessage(200, "Questions with answer options retrieved successfully");
            returnObj.setReturnObject(questionArray);
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Error: " + e.getMessage());
        }

        return returnObj;
    }

    public OperationReturnObject updateQuestionAndAnswers(JSONObject request) {
        requiresAuth();
        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long questionId = data.getLong("questionId");

            // Fetch the question
            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new NoSuchElementException("Question not found"));

            // Optional update to question title
            if (data.containsKey("questionTitle")) {
                String newQuestionTitle = data.getString("questionTitle");
                question.setContent(newQuestionTitle);
                questionRepository.save(question);
            }

            // Optional answerOptions update
            if (data.containsKey("answerOptions")) {
                JSONArray answerOptionsArray = data.getJSONArray("answerOptions");

                for (int i = 0; i < answerOptionsArray.size(); i++) {
                    JSONObject answerObj = answerOptionsArray.getJSONObject(i);

                    Long answerId = answerObj.getLong("id");

                    AnswerOption answer = answerOptionRepository.findById(answerId)
                            .orElseThrow(() -> new NoSuchElementException("Answer option not found with id: " + answerId));

                    // Only update if field is present
                    if (answerObj.containsKey("text")) {
                        answer.setAnswerText(answerObj.getString("text"));
                    }

                    if (answerObj.containsKey("isCorrect")) {
                        answer.setCorrect(answerObj.getBoolean("isCorrect"));
                    }

                    answerOptionRepository.save(answer);
                }
            }

            returnObj.setReturnCodeAndReturnMessage(200, "Question and answers updated successfully");
            returnObj.setReturnObject(question);
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, "Update failed: " + e.getMessage());
        }

        return returnObj;
    }

    @Transactional
    public OperationReturnObject deleteTopicTest(JSONObject request) {

        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long testId = data.getLong("testId");

            Test test = testRepository.findById(testId)
                    .orElseThrow(() -> new NoSuchElementException("Test not found"));

            // Delete answer options first
            List<Question> questions = questionRepository.findByTestId(test.getId().intValue());
            for (Question question : questions) {
                answerOptionRepository.deleteByQuestionId(question.getId().intValue());
            }

            // Delete questions
            questionRepository.deleteAll(questions);

            // Delete the test itself
            testRepository.delete(test);

            returnObj.setReturnCodeAndReturnMessage(200, "Test deleted successfully.");
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Failed to delete test: " + e.getMessage());
        }

        return returnObj;
    }

    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "createTopicTest" -> createTopicTest(request);
            case "getTestAttempt" -> getTestAttempt(request);
            case "getQuestionByTestId" -> getQuestionByTestId(request);
            case "updateQuestionAndAnswers"  -> updateQuestionAndAnswers(request);
            case "deleteTopicTest"  -> deleteTopicTest(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
