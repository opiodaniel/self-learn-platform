package com.ceres.project.models.jpa_helpers.dto;

import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

public record CourseCreateRequest(
        Long courseId,
        MultipartFile image,
        String title,
        String description,
        String categoryType
) {

}
