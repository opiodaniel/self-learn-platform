package com.ceres.project.repositories;


import com.ceres.project.models.database.Subtopic;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRepository extends JetRepository<Subtopic, Long> {
    List<Subtopic> findByTopicId(Long topicId);
}

