package com.ceres.project.repositories;


import com.ceres.project.models.database.Topic;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TopicRepository extends JetRepository<Topic, Long> {
    List<Topic> findByCourseId(Long courseId);
}

