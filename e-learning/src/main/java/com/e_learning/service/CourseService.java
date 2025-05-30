package com.e_learning.service;

import com.e_learning.dto.CourseResponseDTO;
import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Category;
import com.e_learning.model.Course;
import com.e_learning.repository.CategoryRepository;
import com.e_learning.repository.CourseRepository;
import com.e_learning.repository.CourseVoteRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class CourseService {

    private final CourseRepository courseRepository;
    private final CourseVoteRepository voteRepository;
    private final CourseCommentService commentService;
    private final CategoryRepository categoryRepository;

    private CourseService(CourseRepository courseRepository, CourseVoteRepository voteRepository, CourseCommentService commentService, CategoryRepository categoryRepository){
        this.courseRepository =courseRepository;
        this.voteRepository =voteRepository;
        this.commentService =commentService;
        this.categoryRepository = categoryRepository;
    }

//    public Course createCourse(Course course) {
//        return courseRepository.save(course);
//    }

    public Course createCourse(Course course) {
        // Ensure the category exists and attach the full entity
        if (course.getCategory() != null && course.getCategory().getId() != null) {
            Category category = categoryRepository.findById(course.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + course.getCategory().getId()));
            course.setCategory(category);
        } else {
            throw new IllegalArgumentException("Category is required when creating a course.");
        }

        return courseRepository.save(course);
    }

    public List<Course> getCoursesByCategoryId(Long categoryId) {
        return courseRepository.findByCategoryId(categoryId);
    }


//    public List<Course> getAllCourses() {
//        return courseRepository.findAll();
//    }

    public List<CourseResponseDTO> getAllCoursesWithStats() {
        List<Course> courses = courseRepository.findAll();

        return courses.stream().map(course -> {
            int topicCount = course.getTopics().size();
            int subtopicCount = course.getTopics().stream()
                    .mapToInt(topic -> topic.getSubtopics().size())
                    .sum();

            long upvoteCount = voteRepository.countUpvotesByCourseId(course.getId());
            long downvoteCount = voteRepository.countDownvotesByCourseId(course.getId());
            long commentCount = commentService.getCommentCountByCourseId(course.getId());

            CourseResponseDTO dto = new CourseResponseDTO();
            dto.setCourseId(course.getId());
            dto.setCourseTitle(course.getTitle());
            dto.setCourseDescription(course.getDescription());
            dto.setCourseImg(course.getImageUrl()); // Uncomment if applicable

            dto.setCourseTopicCount(topicCount);
            dto.setCourseSubtopicCount(subtopicCount);
            dto.setCourseUpvoteCount(upvoteCount);
            dto.setCourseDownvoteCount(downvoteCount);
            dto.setCourseCommentCount(commentCount);  // ← Newly added line

            return dto;
        }).collect(Collectors.toList());
    }



    public Optional<Course> getCourseById(Long id) {
        return courseRepository.findById(id);
    }

//    public Course updateCourse(Long id, Course updatedCourse) {
//        Course existing = courseRepository.findById(id)
//                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
//
//        if (updatedCourse.getTitle() != null) {
//            existing.setTitle(updatedCourse.getTitle());
//        }
//        if (updatedCourse.getDescription() != null) {
//            existing.setDescription(updatedCourse.getDescription());
//        }
//
//        return courseRepository.save(existing);
//    }

    public Course updateCourse(Long id, Course updatedCourse) {
        Course existing = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));

        if (updatedCourse.getTitle() != null) {existing.setTitle(updatedCourse.getTitle());}
        if (updatedCourse.getDescription() != null) {existing.setDescription(updatedCourse.getDescription());}

        if (updatedCourse.getCategory() != null && updatedCourse.getCategory().getId() != null) {
            Category category = categoryRepository.findById(updatedCourse.getCategory().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Category not found with id: " + updatedCourse.getCategory().getId()));
            existing.setCategory(category);
        }
        return courseRepository.save(existing);
    }


    public void deleteCourse(Long id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + id));
        courseRepository.delete(course);
    }



}
