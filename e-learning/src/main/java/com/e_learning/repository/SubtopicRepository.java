package com.e_learning.repository;


import com.e_learning.model.Subtopic;
import com.e_learning.model.Topic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubtopicRepository extends JpaRepository<Subtopic, Long> {
    List<Subtopic> findByTopicId(Long topicId);
}

