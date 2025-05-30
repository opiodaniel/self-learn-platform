package com.e_learning.dto;

import java.time.LocalDateTime;

public class CourseCommentResponse {
    private Long id;
    private String content;
    private LocalDateTime createdAt;
    private String username;

    public CourseCommentResponse(Long id, String content, LocalDateTime createdAt, String username) {
        this.id = id;
        this.content = content;
        this.createdAt = createdAt;
        this.username = username;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getContent() {
        return content;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public String getUsername() {
        return username;
    }
}

