package com.e_learning.dto;

public class CourseResponseDTO {
    private Long courseId;
    private String courseImg;
    private String courseTitle;
    private String courseDescription;

    private int courseTopicCount;
    private int courseSubtopicCount;

    private long courseUpvoteCount;
    private long courseDownvoteCount;
    private long courseCommentCount;



    public CourseResponseDTO() {
    }

    public CourseResponseDTO(Long courseId, String courseImg, String courseTitle, String courseDescription, int courseTopicCount, int courseSubtopicCount, long courseUpvoteCount, long courseDownvoteCount, long courseCommentCount) {
        this.courseId = courseId;
        this.courseImg = courseImg;
        this.courseTitle = courseTitle;
        this.courseDescription = courseDescription;
        this.courseTopicCount = courseTopicCount;
        this.courseSubtopicCount = courseSubtopicCount;
        this.courseUpvoteCount = courseUpvoteCount;
        this.courseDownvoteCount = courseDownvoteCount;
        this.courseCommentCount = courseCommentCount;
    }

    public Long getCourseId() {
        return courseId;
    }

    public void setCourseId(Long courseId) {
        this.courseId = courseId;
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

    public String getCourseImg() {
        return courseImg;
    }

    public void setCourseImg(String courseImg) {
        this.courseImg = courseImg;
    }

    public int getCourseTopicCount() {
        return courseTopicCount;
    }

    public void setCourseTopicCount(int courseTopicCount) {
        this.courseTopicCount = courseTopicCount;
    }

    public int getCourseSubtopicCount() {
        return courseSubtopicCount;
    }

    public void setCourseSubtopicCount(int courseSubtopicCount) {
        this.courseSubtopicCount = courseSubtopicCount;
    }

    public long getCourseUpvoteCount() {
        return courseUpvoteCount;
    }

    public void setCourseUpvoteCount(long courseUpvoteCount) {
        this.courseUpvoteCount = courseUpvoteCount;
    }

    public long getCourseDownvoteCount() {
        return courseDownvoteCount;
    }

    public void setCourseDownvoteCount(long courseDownvoteCount) {
        this.courseDownvoteCount = courseDownvoteCount;
    }

    public long getCourseCommentCount() {
        return courseCommentCount;
    }

    public void setCourseCommentCount(long courseCommentCount) {
        this.courseCommentCount = courseCommentCount;
    }
}


