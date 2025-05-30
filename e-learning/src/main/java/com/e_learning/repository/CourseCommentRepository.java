package com.e_learning.repository;

import com.e_learning.model.CourseComment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CourseCommentRepository extends JpaRepository<CourseComment, Long> {
    long countByCourseId(Long courseId);
    List<CourseComment> findByCourseIdOrderByCreatedAtDesc(Long courseId);
}

