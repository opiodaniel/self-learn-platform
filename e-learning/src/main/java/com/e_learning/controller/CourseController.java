package com.e_learning.controller;

import com.e_learning.dto.CourseResponseDTO;
import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Category;
import com.e_learning.model.Course;
import com.e_learning.repository.CategoryRepository;
import com.e_learning.service.CategoryService;
import com.e_learning.service.CourseService;
import com.e_learning.service.GoogleCloudStorageService;
import com.e_learning.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/v1/courses")
public class CourseController {

    private final CourseService courseService;
    private final ResponseService responseService;
    private final GoogleCloudStorageService storageService;
    private final CategoryService categoryService;

    public CourseController(CourseService courseService, ResponseService responseService, GoogleCloudStorageService storageService, CategoryService categoryService) {
        this.courseService = courseService;
        this.responseService = responseService;
        this.storageService = storageService;
        this.categoryService = categoryService;
    }


    //    @PostMapping()
//    public ResponseEntity<Map<String, Object>> createCourse(@RequestBody Course course) {
//        try {
//            Course createdCourse = courseService.createCourse(course);
//            return responseService.createSuccessResponse(201, createdCourse, HttpStatus.CREATED);
//        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
//            Map<String, String> error = Map.of("category", ex.getMessage());
//            return responseService.createErrorResponse(400, error, HttpStatus.BAD_REQUEST);
//        }
//    }


    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Map<String, Object>> createCourse(
            @RequestParam("title") String title,
            @RequestParam("description") String description,
            @RequestParam("categoryId") Long categoryId,
            @RequestParam(value = "image", required = false) MultipartFile image
    ) throws IOException {
        try {
            Course course = new Course();
            course.setTitle(title);
            course.setDescription(description);

            // If image is provided, upload and set the URL
            if (image != null && !image.isEmpty()) {
                String imageUrl = storageService.uploadFile(image); // make sure this returns a valid URL
                course.setImageUrl(imageUrl); // ensure this field exists in your Course entity
            }

            Category category = categoryService.getCategoryById(categoryId);
            course.setCategory(category);

            Course createdCourse = courseService.createCourse(course);
            return responseService.createSuccessResponse(201, createdCourse, HttpStatus.CREATED);
        } catch (IllegalArgumentException | ResourceNotFoundException ex) {
            Map<String, String> error = Map.of("error", ex.getMessage());
            return responseService.createErrorResponse(400, error, HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<Map<String, Object>> getCoursesByCategory(@PathVariable Long categoryId) {
        List<Course> courses = courseService.getCoursesByCategoryId(categoryId);
        return responseService.createSuccessResponse(200, courses, HttpStatus.OK);
    }

//    @GetMapping
//    public ResponseEntity<Map<String, Object>> getAllCourses() {
//        List<Course> courses = courseService.getAllCourses();
//        return responseService.createSuccessResponse(200, courses, HttpStatus.OK);
//    }

    @GetMapping
    public ResponseEntity<Map<String, Object>> getAllCourses() {
        List<CourseResponseDTO> courseStats = courseService.getAllCoursesWithStats();
        return responseService.createSuccessResponse(200, courseStats, HttpStatus.OK);
    }


    @GetMapping("/{courseId}")
    public ResponseEntity<Map<String, Object>> getCourseById(@PathVariable Long courseId) {
        Optional<Course> course = courseService.getCourseById(courseId);
        return responseService.createSuccessResponse(200, course, HttpStatus.OK);
    }

    @PutMapping("/update/course/{id}")
    public ResponseEntity<Map<String, Object>> updateCourse(@PathVariable Long id, @RequestBody Course updatedCourse) {
        Course course = courseService.updateCourse(id, updatedCourse);
        return responseService.createSuccessResponse(200, course, HttpStatus.OK);
    }

    @DeleteMapping("/delete/course/{id}")
    public ResponseEntity<Map<String, Object>> deleteCourse(@PathVariable Long id) {
        courseService.deleteCourse(id);
        return responseService.createSuccessResponse(200, "Course deleted successfully", HttpStatus.OK);
    }


}

