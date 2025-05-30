package com.e_learning.repository;

import com.e_learning.dto.EnrolledUserDTO;
import com.e_learning.dto.EnrollmentDTO;
import com.e_learning.model.Course;
import com.e_learning.model.Enrollment;
import com.e_learning.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface EnrollmentRepository extends JpaRepository<Enrollment, Long> {
    Optional<Enrollment> findByUserAndCourse(User user, Course course);

    @Query("SELECT new com.e_learning.dto.EnrollmentDTO(e.id, CONCAT(u.firstName, ' ', u.lastName), u.username, c.title, c.description, e.enrollmentDate, e.status, e.progressPercentage) " +
            "FROM Enrollment e JOIN e.user u JOIN e.course c")
    List<EnrollmentDTO> fetchAllEnrollments();

    @Query("SELECT new com.e_learning.dto.EnrolledUserDTO(" +
            "u.id, CONCAT(u.firstName, ' ', u.lastName), u.username, u.email, e.enrollmentDate) " +
            "FROM Enrollment e JOIN e.user u WHERE e.course.id = :courseId")
    List<EnrolledUserDTO> findUsersByCourseId(@Param("courseId") Long courseId);

    List<Enrollment> findByUser(User user);



}
