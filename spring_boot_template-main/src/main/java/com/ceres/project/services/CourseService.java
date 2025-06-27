package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.Course;
import com.ceres.project.models.database.SystemUserModel;
import com.ceres.project.models.jpa_helpers.dto.CourseCreateRequest;
import com.ceres.project.models.jpa_helpers.enums.CourseCategory;
import com.ceres.project.repositories.CourseRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import com.ceres.project.utils.gcp_storage.FileServiceImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.EnumUtils;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class CourseService extends BaseWebActionsService {

    private final CourseRepository courseRepository;
    private final FileServiceImpl fileService;

    // Create Course
    public Course createCourse(CourseCreateRequest request){
        requiresAuth();

        Course course = new Course();
        course.setTitle(request.title());
        course.setDescription(request.description());
        course.setCreatedAt(LocalDateTime.now());
        course.setPublished(false);

        String categoryTypeStr = request.categoryType();


        if (!EnumUtils.isValidEnum(CourseCategory.class, categoryTypeStr)) {
            throw new RuntimeException("Invalid category: '" + categoryTypeStr +
                    "'. Allowed values: " + Arrays.toString(CourseCategory.values()));
        }
        course.setCategoryType(categoryTypeStr);

        if (request.image() != null && !request.image().isEmpty()) {
            var course_image = fileService.uploadFile(request.image());
            course.setImageUrl(course_image.getUrl());
        }

        return courseRepository.save(course);
    }

    // Publish and Unpublish the course. //
    public OperationReturnObject publishCourse(JSONObject request) {
        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            Long courseId = request.getLong("courseId");

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            course.setPublished(true);
            courseRepository.save(course);

            returnObject.setCodeAndMessageAndReturnObject(200, "Course published successfully", course);

        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Failed to publish course");
        }
        return returnObject;
    }


    public Course updateCourse(CourseCreateRequest request) {
        requiresAuth();

        Long courseId = request.courseId();

        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new RuntimeException("Course not found"));

        if (request.title() != null && !request.title().isBlank()) {
            course.setTitle(request.title());
        }

        if (request.description() != null && !request.description().isBlank()) {
            course.setDescription(request.description());
        }

        String categoryTypeStr = request.categoryType();
        if (categoryTypeStr != null && !categoryTypeStr.isBlank()) {
            if (!EnumUtils.isValidEnum(CourseCategory.class, categoryTypeStr)) {
                throw new RuntimeException("Invalid category: '" + categoryTypeStr +
                        "'. Allowed values: " + Arrays.toString(CourseCategory.values()));
            }
            course.setCategoryType(categoryTypeStr);
        }

        if (request.image() != null && !request.image().isEmpty()) {
            var courseImage = fileService.uploadFile(request.image());
            course.setImageUrl(courseImage.getUrl());
        }

        return courseRepository.save(course);
    }

    // Delete Course //
    public OperationReturnObject deleteCourse(JSONObject request) {
        requiresAuth();
        OperationReturnObject returnObject = new OperationReturnObject();

        try {
            Long courseId = request.getJSONObject("data").getLong("courseId");

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course with ID " + courseId + " not found"));

            courseRepository.delete(course);
            returnObject.setCodeAndMessageAndReturnObject(200, "Course deleted successfully", course);

        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Failed to delete course");
        }
        return returnObject;
    }

    public OperationReturnObject getCourseById(JSONObject request) {

        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            JSONObject data = request.getJSONObject("data");
            requires(List.of("courseId"), data);

            Long courseId = data.getLong("courseId");
            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new NoSuchElementException("Course not found"));

            JSONObject courseJson = new JSONObject();
            courseJson.put("id", course.getId());
            courseJson.put("courseImg", course.getImageUrl());
            courseJson.put("title", course.getTitle());
            courseJson.put("description", course.getDescription());


            returnObject.setCodeAndMessageAndReturnObject(200, "Course with topics fetched successfully", courseJson);

        } catch (NoSuchElementException e) {
            returnObject.setReturnCodeAndReturnMessage(404, "Course not found with given ID");
        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Unable to fetch course");
        }

        return returnObject;
    }

    // Get Courses By Category
    public OperationReturnObject getCoursesByCategoryType(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObject = new OperationReturnObject();
        try {
            JSONObject data = request.getJSONObject("data");
            requires(List.of("category"), data);
            String category = data.getString("category");
            List<Course> courses = courseRepository.findByCategoryType(category);
            returnObject.setCodeAndMessageAndReturnObject(200, "Courses fetched successfully", courses);

        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Unable to fetch courses by category");
        }

        return returnObject;
    }

    // Get All Courses
    public OperationReturnObject getAllCourses(JSONObject request) {

        OperationReturnObject returnObject = new OperationReturnObject();

        try {
            List<Course> courses = courseRepository.findAll();
            JSONArray courseArray = new JSONArray();

            for (Course course : courses) {

                JSONObject courseJson = new JSONObject();
                courseJson.put("id", course.getId());
                courseJson.put("title", course.getTitle());
                courseJson.put("description", course.getDescription());
                courseJson.put("courseImg", course.getImageUrl());
                courseJson.put("categoryType", course.getCategoryType());
                courseJson.put("published", course.isPublished());

                courseArray.add(courseJson);
            }

            returnObject.setCodeAndMessageAndReturnObject(200, "Courses fetched successfully", courseArray);

        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(400, "Unable to fetch courses");
        }

        return returnObject;
    }


    public OperationReturnObject getUserProgress(JSONObject request) {
        requiresAuth();
        SystemUserModel user = authenticatedUser();
        OperationReturnObject returnObject = new OperationReturnObject();

        try {
            JSONObject data = request.getJSONObject("data");
            requires(List.of("courseId"), data);
            Long courseId = data.getLong("courseId");
            String userId = user.getServpaceId();

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new RuntimeException("Course not found"));

            JSONObject response = new JSONObject();
            response.put("courseId", course.getId());
            response.put("courseTitle", course.getTitle());

            returnObject.setReturnCodeAndReturnMessage(200, "Progress retrieved successfully");
            returnObject.setReturnObject(response);
        } catch (Exception e) {
            returnObject.setReturnCodeAndReturnMessage(500, "Error: " + e.getMessage());
        }

        return returnObject;
    }


    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "publishCourse" -> publishCourse(request);
            case "getCourseById" -> getCourseById(request);
            case "getCoursesByCategoryType" -> getCoursesByCategoryType(request);
            case "getAllCourses" -> getAllCourses(request);
            case "getUserProgress" -> getUserProgress(request);
            case "deleteCourse" -> deleteCourse(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}

