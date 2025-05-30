package com.e_learning.model;

import jakarta.persistence.*;

// === AnswerOption.java ===
@Entity
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String answerText;

    private boolean isCorrect;

    @ManyToOne(optional = false)
    private Question question;

    public AnswerOption() {
    }

    public AnswerOption(Question question, boolean isCorrect, String answerText, Long id) {
        this.question = question;
        this.isCorrect = isCorrect;
        this.answerText = answerText;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAnswerText() {
        return answerText;
    }

    public void setAnswerText(String answerText) {
        this.answerText = answerText;
    }

    public boolean isCorrect() {
        return isCorrect;
    }

    public void setCorrect(boolean correct) {
        isCorrect = correct;
    }

    public Question getQuestion() {
        return question;
    }

    public void setQuestion(Question question) {
        this.question = question;
    }
}
