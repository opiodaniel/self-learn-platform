package com.e_learning.model;

import jakarta.persistence.*;

import java.util.List;

// === UserAnswer.java ===
@Entity
public class UserAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    private User user;

    @ManyToOne(optional = false)
    private Question question;

    @ManyToOne(optional = false)
    private TestSubmission submission;

    @ManyToMany
    private List<AnswerOption> selectedOptions;

    public UserAnswer() {
    }



    public Long getId() {
        return id;
    }

    public UserAnswer(Long id, User user, Question question, TestSubmission submission, List<AnswerOption> selectedOptions) {
        this.id = id;
        this.user = user;
        this.question = question;
        this.submission = submission;
        this.selectedOptions = selectedOptions;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }

    public List<AnswerOption> getSelectedOptions() {
        return selectedOptions;
    }

    public void setSelectedOptions(List<AnswerOption> selectedOptions) {
        this.selectedOptions = selectedOptions;
    }

    public TestSubmission getSubmission() {
        return submission;
    }

    public void setSubmission(TestSubmission submission) {
        this.submission = submission;
    }
}
