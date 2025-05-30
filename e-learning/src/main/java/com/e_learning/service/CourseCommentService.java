package com.e_learning.service;

import com.e_learning.dto.CourseCommentResponse;
import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Course;
import com.e_learning.model.CourseComment;
import com.e_learning.model.User;
import com.e_learning.repository.CourseCommentRepository;
import com.e_learning.repository.CourseRepository;
import com.e_learning.repository.UserRepository;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CourseCommentService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseCommentRepository commentRepository;

    public CourseCommentService(CourseRepository courseRepository,
                                UserRepository userRepository,
                                CourseCommentRepository commentRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.commentRepository = commentRepository;
    }

    public CourseComment addComment(Long courseId, String username, String content) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CourseComment comment = new CourseComment();
        comment.setContent(content);
        comment.setCourse(course);
        comment.setUser(user);
        comment.setCreatedAt(LocalDateTime.now());

        return commentRepository.save(comment);
    }

//    public List<CourseComment> getCommentsByCourseId(Long courseId) {
//        return commentRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
//    }

    public long getCommentCountByCourseId(Long courseId) {
        return commentRepository.countByCourseId(courseId);
    }

    public List<CourseCommentResponse> getCommentsByCourseId(Long courseId) {
        List<CourseComment> comments = commentRepository.findByCourseIdOrderByCreatedAtDesc(courseId);
        return comments.stream()
                .map(comment -> new CourseCommentResponse(
                        comment.getId(),
                        comment.getContent(),
                        comment.getCreatedAt(),
                        comment.getUser().getUsername()))
                .collect(Collectors.toList());
    }



    public CourseComment updateComment(Long commentId, String updatedText, String username) {
        CourseComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only update your own comment.");
        }

        comment.setContent(updatedText);
        return commentRepository.save(comment);
    }

    public void deleteComment(Long commentId, String username) {
        CourseComment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));

        if (!comment.getUser().getUsername().equals(username)) {
            throw new AccessDeniedException("You can only delete your own comment.");
        }

        commentRepository.delete(comment);
    }
}

//@Service
//public class CourseCommentService {
//
//    private final CourseRepository courseRepository;
//    private final UserRepository userRepository;
//    private final CourseCommentRepository commentRepository;
//
//    public CourseCommentService(CourseRepository courseRepository,
//                                UserRepository userRepository,
//                                CourseCommentRepository commentRepository) {
//        this.courseRepository = courseRepository;
//        this.userRepository = userRepository;
//        this.commentRepository = commentRepository;
//    }
//
//    public CourseComment addComment(Long courseId, String username, CourseComment comment) {
//        Course course = courseRepository.findById(courseId)
//                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));
//
//        User user = userRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
//
//        comment.setCourse(course);
//        comment.setUser(user);
//        comment.setCreatedAt(LocalDateTime.now());
//        return commentRepository.save(comment);
//    }
//
//    public long getCommentCountByCourseId(Long courseId) {
//        return commentRepository.countByCourseId(courseId);
//    }
//
//    public List<CourseComment> getCommentsByCourseId(Long courseId) {
//        return commentRepository.findByCourseId(courseId);
//    }
//
//
//    public CourseComment updateComment(Long commentId, String updatedText) {
//        CourseComment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
//        comment.setContent(updatedText);
//        return commentRepository.save(comment);
//    }
//
//    public void deleteComment(Long commentId) {
//        CourseComment comment = commentRepository.findById(commentId)
//                .orElseThrow(() -> new ResourceNotFoundException("Comment not found with id: " + commentId));
//        commentRepository.delete(comment);
//    }
//
//}

