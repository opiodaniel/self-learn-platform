package com.e_learning.dto;

import jakarta.validation.constraints.NotBlank;

public class CourseCommentRequest {
    @NotBlank(message = "Content cannot be blank")
    private String content;
    private String username;


    // Getter and Setter
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }


}

