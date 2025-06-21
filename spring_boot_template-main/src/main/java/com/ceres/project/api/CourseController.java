package com.ceres.project.api;


import com.ceres.project.models.database.Course;
import com.ceres.project.models.jpa_helpers.dto.CourseCreateRequest;
import com.ceres.project.services.CourseService;
import com.ceres.project.utils.MasterResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/course/")
@RequiredArgsConstructor
@Slf4j
public class CourseController {

    private final CourseService courseService;

    @PostMapping("create")
    public ResponseEntity<Map<String, Object>> createCourse(@ModelAttribute CourseCreateRequest request) {
        MasterResponse response = new MasterResponse();
        try {
            Course course = courseService.createCourse(request);
            return response.ResponseOk200("Course Created successfully", course);
        } catch (Exception e) {
            return response.ResponseError400(e.getMessage());
        }
    }
}
