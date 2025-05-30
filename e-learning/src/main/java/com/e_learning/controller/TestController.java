package com.e_learning.controller;

import com.e_learning.dto.AnswerOptionDTO;
import com.e_learning.dto.CreateTestRequest;
import com.e_learning.dto.QuestionDTO;
import com.e_learning.model.AnswerOption;
import com.e_learning.model.Question;
import com.e_learning.model.Test;
import com.e_learning.model.TestSubmission;
import com.e_learning.service.ResponseService;
import com.e_learning.service.TestService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/tests")
public class TestController {

    private final TestService testService;
    private final ResponseService responseService;

    public TestController(TestService testService, ResponseService responseService) {
        this.testService = testService;
        this.responseService = responseService;
    }

    @PostMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> createTest(
            @PathVariable Long topicId,
            @RequestBody CreateTestRequest request
    ) {
        try {
            Test test = testService.createTest(topicId, request.getTitle());
            return responseService.createSuccessResponse(200, test, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> errors = Map.of("testCreationError", ex.getMessage());
            return responseService.createErrorResponse(400, errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/by-topic/{topicId}")
    public ResponseEntity<Map<String, Object>> getTestByTopic(@PathVariable Long topicId) {
        try {
            Test test = testService.getTestByTopic(topicId);
            return responseService.createSuccessResponse(200, test, HttpStatus.OK);
        } catch (RuntimeException ex) {
            Map<String, String> errors = Map.of("testFetchError", ex.getMessage());
            return responseService.createErrorResponse(404, errors, HttpStatus.NOT_FOUND);
        }
    }

    @PostMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> addQuestion(@PathVariable Long testId, @RequestBody QuestionDTO dto) {
        try {
            Question question = testService.addQuestion(testId, dto.getContent(), dto.getMultipleAnswersAllowed());
            return responseService.createSuccessResponse(200, question, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> errors = Map.of("questionError", ex.getMessage());
            return responseService.createErrorResponse(400, errors, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/questions/{questionId}/answers")
    public ResponseEntity<Map<String, Object>> addAnswer(@PathVariable Long questionId, @RequestBody AnswerOptionDTO dto) {
        try {
            AnswerOption answer = testService.addAnswerOption(questionId, dto.getAnswerText(), dto.getCorrect());
            return responseService.createSuccessResponse(200, answer, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> errors = Map.of("answerError", ex.getMessage());
            return responseService.createErrorResponse(400, errors, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/{testId}/submit")
    public ResponseEntity<Map<String, Object>> submitTest(
            @PathVariable Long testId,
            @RequestParam Long userId,
            @RequestBody Map<Long, List<Long>> questionAnswersMap
    ) {
        try {
            TestSubmission submission = testService.submitTest(testId, userId, questionAnswersMap);
            return responseService.createSuccessResponse(200, submission, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> errors = Map.of("submissionError", ex.getMessage());
            return responseService.createErrorResponse(400, errors, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/submissions/user/{userId}")
    public ResponseEntity<Map<String, Object>> getUserSubmissions(@PathVariable Long userId) {
        List<TestSubmission> submissions = testService.getUserSubmissions(userId);
        return responseService.createSuccessResponse(200, submissions, HttpStatus.OK);
    }

    @GetMapping("/{testId}/submissions")
    public ResponseEntity<Map<String, Object>> getSubmissionsByTest(@PathVariable Long testId) {
        List<TestSubmission> submissions = testService.getSubmissionsByTest(testId);
        return responseService.createSuccessResponse(200, submissions, HttpStatus.OK);
    }

    @GetMapping("/submissions/{submissionId}")
    public ResponseEntity<Map<String, Object>> getSubmissionDetails(@PathVariable Long submissionId) {
        TestSubmission submission = testService.getSubmissionDetails(submissionId);
        return responseService.createSuccessResponse(200, submission, HttpStatus.OK);
    }

    @GetMapping("/{testId}/questions")
    public ResponseEntity<Map<String, Object>> getQuestionsByTest(@PathVariable Long testId) {
        List<Question> questions = testService.getQuestionsByTest(testId);
        return responseService.createSuccessResponse(200, questions, HttpStatus.OK);
    }

    @GetMapping("/questions/{questionId}/answers")
    public ResponseEntity<Map<String, Object>> getAnswersByQuestion(@PathVariable Long questionId) {
        List<AnswerOption> answers = testService.getAnswersByQuestion(questionId);
        return responseService.createSuccessResponse(200, answers, HttpStatus.OK);
    }
}

