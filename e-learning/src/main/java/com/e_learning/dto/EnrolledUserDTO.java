package com.e_learning.dto;

import java.time.LocalDate;

public class EnrolledUserDTO {
    private Long userId;
    private String fullName;
    private String username;
    private String email;
    private LocalDate enrollmentDate;

    public EnrolledUserDTO(Long userId, String fullName, String username, String email, LocalDate enrollmentDate) {
        this.userId = userId;
        this.fullName = fullName;
        this.username = username;
        this.email = email;
        this.enrollmentDate = enrollmentDate;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }
}

