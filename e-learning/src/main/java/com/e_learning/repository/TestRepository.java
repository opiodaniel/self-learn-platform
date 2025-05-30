package com.e_learning.repository;

import com.e_learning.model.Test;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TestRepository extends JpaRepository<Test, Long> {
    //List<Test> findByTopicId(Long topicId);
    Optional<Test> findByTopicId(Long topicId);

}
