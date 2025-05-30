package com.e_learning.service;

import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.*;
import com.e_learning.repository.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestService {

    private final TestRepository testRepository;
    private final TopicRepository topicRepository;
    private final QuestionRepository questionRepository;
    private final AnswerOptionRepository answerOptionRepository;
    private final UserAnswerRepository userAnswerRepository;
    private final TestSubmissionRepository testSubmissionRepository;
    private final UserRepository userRepository;

    public TestService(TestRepository testRepository, TopicRepository topicRepository, QuestionRepository questionRepository, AnswerOptionRepository answerOptionRepository, UserAnswerRepository userAnswerRepository, TestSubmissionRepository testSubmissionRepository, UserRepository userRepository) {
        this.testRepository = testRepository;
        this.topicRepository = topicRepository;
        this.questionRepository = questionRepository;
        this.answerOptionRepository = answerOptionRepository;
        this.userAnswerRepository = userAnswerRepository;
        this.testSubmissionRepository = testSubmissionRepository;
        this.userRepository = userRepository;
    }

    public Test createTest(Long topicId, String testTitle) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found"));

        Test test = new Test();
        test.setTitle(testTitle);
        test.setTopic(topic);
        return testRepository.save(test);
    }

    public Test getTestByTopic(Long topicId) {
        return testRepository.findByTopicId(topicId)
                .orElseThrow(() -> new RuntimeException("No test found for topic with ID: " + topicId));
    }


    public Question addQuestion(Long testId, String content, Boolean multiAnswer) {
        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        Question question = new Question();
        question.setTest(test);
        question.setContent(content);
        question.setMultipleAnswersAllowed(multiAnswer);

        return questionRepository.save(question);
    }

    public AnswerOption addAnswerOption(Long questionId, String text, Boolean isCorrect) {
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

        if (!question.isMultipleAnswersAllowed() && isCorrect) {
            boolean alreadyHasCorrect = answerOptionRepository.existsByQuestionIdAndIsCorrectTrue(questionId);
            if (alreadyHasCorrect) {
                throw new IllegalStateException("Only one correct answer is allowed for this question.");
            }
        }

        AnswerOption option = new AnswerOption();
        option.setQuestion(question);
        option.setAnswerText(text);
        option.setCorrect(isCorrect);

        return answerOptionRepository.save(option);
    }

    public TestSubmission submitTest(Long testId, Long userId, Map<Long, List<Long>> questionAnswersMap) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Test test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("Test not found"));

        int correctAnswers = 0;
        int totalQuestions = 0;

        TestSubmission submission = new TestSubmission();
        submission.setTest(test);
        submission.setUser(user);
        submission.setSubmittedAt(LocalDateTime.now());
        submission = testSubmissionRepository.save(submission);

        for (Map.Entry<Long, List<Long>> entry : questionAnswersMap.entrySet()) {
            Long questionId = entry.getKey();
            List<Long> selectedAnswerIds = entry.getValue();

            Question question = questionRepository.findById(questionId)
                    .orElseThrow(() -> new ResourceNotFoundException("Question not found"));

            // Fetch correct answers
            List<AnswerOption> correctOptions = answerOptionRepository.findByQuestionIdAndIsCorrectTrue(questionId);
            Set<Long> correctIds = correctOptions.stream().map(AnswerOption::getId).collect(Collectors.toSet());

            // Enforce: multiple-answer questions must have at least 2 correct options
            if (question.isMultipleAnswersAllowed() && correctIds.size() < 2) {
                throw new IllegalStateException("Question marked as allowing multiple correct answers must have at least 2 correct options.");
            }

            // Compare selected vs correct
            Set<Long> selectedIds = new HashSet<>(selectedAnswerIds);
            if (correctIds.equals(selectedIds)) {
                correctAnswers++;
            }

            // Fetch selected AnswerOption entities
            List<AnswerOption> selectedOptions = selectedAnswerIds.stream()
                    .map(id -> answerOptionRepository.findById(id)
                            .orElseThrow(() -> new ResourceNotFoundException("Answer not found")))
                    .collect(Collectors.toList());

            // Save UserAnswer
            UserAnswer userAnswer = new UserAnswer();
            userAnswer.setUser(user);
            userAnswer.setSubmission(submission);
            userAnswer.setQuestion(question);
            userAnswer.setSelectedOptions(selectedOptions);
            userAnswerRepository.save(userAnswer);

            totalQuestions++;
        }


        submission.setScore((double) correctAnswers / totalQuestions * 100);
        return testSubmissionRepository.save(submission);
    }

    public List<TestSubmission> getUserSubmissions(Long userId) {
        return testSubmissionRepository.findByUserId(userId);
    }

    public List<TestSubmission> getSubmissionsByTest(Long testId) {
        return testSubmissionRepository.findByTestId(testId);
    }

    public TestSubmission getSubmissionDetails(Long submissionId) {
        return testSubmissionRepository.findById(submissionId)
                .orElseThrow(() -> new ResourceNotFoundException("Submission not found"));
    }

    public List<Question> getQuestionsByTest(Long testId) {
        return questionRepository.findByTestId(testId);
    }

    public List<AnswerOption> getAnswersByQuestion(Long questionId) {
        return answerOptionRepository.findByQuestionId(questionId);
    }
}


