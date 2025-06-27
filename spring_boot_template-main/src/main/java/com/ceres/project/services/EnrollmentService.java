package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.Course;
import com.ceres.project.models.database.Enrollment;
import com.ceres.project.models.database.SystemUserModel;
import com.ceres.project.repositories.CourseRepository;
import com.ceres.project.repositories.EnrollmentRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class EnrollmentService extends BaseWebActionsService {

    private final EnrollmentRepository enrollmentRepository;
    private final CourseRepository courseRepository;
//    private final TopicRepository topicRepository;
//    private final SubtopicRepository subtopicRepository;

    public OperationReturnObject enrollToCourse(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Integer courseId = data.getIntValue("courseId");

            SystemUserModel user = authenticatedUser();

            // Check if the user is already enrolled
            Enrollment existing = enrollmentRepository.findByUserIdAndCourseId(user.getServpaceId(), courseId);
            if (existing != null) {
                returnObj.setReturnCodeAndReturnMessage(409, "User already enrolled in this course.");
                return returnObj;
            }

            Enrollment enrollment = new Enrollment();
            enrollment.setUserId(user.getServpaceId());
            enrollment.setCourseId(courseId);
            enrollment.setEnrollmentDate(LocalDate.now());
            enrollment.setStatus("ACTIVE");
            enrollment.setProgressPercentage(0.0);

            enrollmentRepository.save(enrollment);

            returnObj.setReturnObject(enrollment);
            returnObj.setReturnCodeAndReturnMessage(200, "Enrolled successfully.");

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Failed to enroll: " + e.getMessage());
        }

        return returnObj;
    }

    public OperationReturnObject getUserEnrolledCourses(JSONObject request) {

        SystemUserModel user = authenticatedUser();

        OperationReturnObject returnObj = new OperationReturnObject();
        try {
            List<Enrollment> user_course_enrollments = enrollmentRepository.findByUserId(user.getServpaceId());

            JSONArray courseArray = new JSONArray();
            for  (Enrollment enrollment : user_course_enrollments) {

                Optional<Course> course =   courseRepository.findById(enrollment.getCourseId().longValue());
                JSONObject courseJson = new JSONObject();
                courseJson.put("course", course);
                courseJson.put("userId", enrollment.getUserId());
                courseJson.put("enrollmentId", enrollment.getId());
                courseJson.put("enrollmentDate", enrollment.getEnrollmentDate());
                courseJson.put("status", enrollment.getStatus());
                courseJson.put("progressPercentage", enrollment.getProgressPercentage());

                courseArray.add(courseJson);
            }
            returnObj.setCodeAndMessageAndReturnObject(200, "User Courses fetched successfully", courseArray);

        } catch (NoSuchElementException e) {
            returnObj.setReturnCodeAndReturnMessage(404, "User not found with given ID" + e.getMessage());
        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, "Unable to fetch user courses: " + e.getMessage());
        }

        return returnObj;
    }

    public OperationReturnObject checkEnrollment(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Integer courseId = data.getIntValue("courseId");

            SystemUserModel user = authenticatedUser(); // Get the current user
            String userId = user.getServpaceId();

            Enrollment existing = enrollmentRepository.findByUserIdAndCourseId(userId, courseId);

            JSONObject result = new JSONObject();
            result.put("alreadyEnrolled", existing != null);

            returnObj.setReturnCodeAndReturnMessage(200, "Enrollment status checked.");
            returnObj.setReturnObject(result);

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(500, "Error checking enrollment: " + e.getMessage());
        }

        return returnObj;
    }


    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "enrollToCourse" -> enrollToCourse(request);
            case "checkEnrollment" -> checkEnrollment(request);
            case "getUserEnrolledCourses" -> getUserEnrolledCourses(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
