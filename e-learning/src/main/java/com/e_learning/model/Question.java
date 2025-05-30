package com.e_learning.model;

import jakarta.persistence.*;

// === Question.java ===
@Entity
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @ManyToOne(optional = false)
    private Test test;

    private boolean multipleAnswersAllowed; // true = multiple correct answers allowed

    public Question() {
    }

    public Question(Long id, String content, Test test, boolean multipleAnswersAllowed) {
        this.id = id;
        this.content = content;
        this.test = test;
        this.multipleAnswersAllowed = multipleAnswersAllowed;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public boolean isMultipleAnswersAllowed() {
        return multipleAnswersAllowed;
    }

    public void setMultipleAnswersAllowed(boolean multipleAnswersAllowed) {
        this.multipleAnswersAllowed = multipleAnswersAllowed;
    }
}
