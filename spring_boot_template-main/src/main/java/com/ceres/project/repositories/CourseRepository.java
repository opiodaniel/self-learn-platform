package com.ceres.project.repositories;


import com.ceres.project.models.database.Course;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CourseRepository extends JetRepository<Course, Long> {
    List<Course> findByCategoryType(String categoryType);
}
