package com.e_learning.controller;

import com.e_learning.dto.EnrolledUserDTO;
import com.e_learning.dto.EnrollmentDTO;
import com.e_learning.model.Course;
import com.e_learning.model.Enrollment;
import com.e_learning.service.EnrollmentService;
import com.e_learning.service.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;
    private final ResponseService responseService;

    private EnrollmentController(EnrollmentService enrollmentService, ResponseService responseService){
        this.enrollmentService = enrollmentService;
        this.responseService = responseService;
    }

    @PostMapping("/{userId}/enroll/{courseId}")
    public ResponseEntity<Map<String, Object>> enrollUser(@PathVariable Long userId, @PathVariable Long courseId) {
        try {
            Enrollment enrollment = enrollmentService.enrollUser(userId, courseId);
            return responseService.createSuccessResponse(200, enrollment, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            Map<String, String> errors = new HashMap<>();
            errors.put("enrollmentError", ex.getMessage());
            return responseService.createErrorResponse(400, errors, HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllEnrollments() {
        List<EnrollmentDTO> enrollments = enrollmentService.getAllEnrollments();
        return responseService.createSuccessResponse(200, enrollments, HttpStatus.OK);
    }

    @GetMapping("/course/{courseId}/users")
    public ResponseEntity<Map<String, Object>> getUserEnrolledCourse(@PathVariable Long courseId) {
        List<EnrolledUserDTO> users = enrollmentService.getUsersByCourseId(courseId);
        return responseService.createSuccessResponse(200, users, HttpStatus.OK);
    }


    @GetMapping("/me")
    public ResponseEntity<Map<String, Object>> getMyEnrollments(Authentication authentication) {
        String username = authentication.getName();
        List<Course> enrolledCourses = enrollmentService.getCoursesByUsername(username);
        return responseService.createSuccessResponse(200, enrolledCourses, HttpStatus.OK);
    }

}
