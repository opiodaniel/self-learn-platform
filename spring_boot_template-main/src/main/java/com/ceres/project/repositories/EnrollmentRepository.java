package com.ceres.project.repositories;


import com.ceres.project.models.database.Enrollment;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;

import java.util.List;

public interface EnrollmentRepository extends JetRepository<Enrollment, Long> {
    Enrollment findByUserIdAndCourseId(String userId, Integer courseId);
    List<Enrollment> findByUserId(String userId);
}
