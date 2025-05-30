package com.e_learning.dto;

import java.time.LocalDate;

public class EnrollmentDTO {
    private Long enrollmentId;
    private String userFullName;
    private String username;
    private String courseTitle;
    private String courseDescription;
    private LocalDate enrollmentDate;
    private String status;
    private Double progressPercentage;

    // constructor, getters, setters

    public EnrollmentDTO(Long enrollmentId, String userFullName, String username, String courseTitle, String courseDescription, LocalDate enrollmentDate, String status, Double progressPercentage) {
        this.enrollmentId = enrollmentId;
        this.userFullName = userFullName;
        this.username = username;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.enrollmentDate = enrollmentDate;
        this.status = status;
        this.progressPercentage = progressPercentage;
    }

    public Long getEnrollmentId() {
        return enrollmentId;
    }

    public void setEnrollmentId(Long enrollmentId) {
        this.enrollmentId = enrollmentId;
    }

    public String getUserFullName() {
        return userFullName;
    }

    public void setUserFullName(String userFullName) {
        this.userFullName = userFullName;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getCourseTitle() {
        return courseTitle;
    }

    public void setCourseTitle(String courseTitle) {
        this.courseTitle = courseTitle;
    }

    public String getCourseDescription() {
        return courseDescription;
    }

    public void setCourseDescription(String courseDescription) {
        this.courseDescription = courseDescription;
    }

    public LocalDate getEnrollmentDate() {
        return enrollmentDate;
    }

    public void setEnrollmentDate(LocalDate enrollmentDate) {
        this.enrollmentDate = enrollmentDate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Double getProgressPercentage() {
        return progressPercentage;
    }

    public void setProgressPercentage(Double progressPercentage) {
        this.progressPercentage = progressPercentage;
    }
}


