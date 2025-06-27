package com.ceres.project.repositories;

import com.ceres.project.models.database.Test;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TestRepository extends JetRepository<Test, Long> {

    @Query("SELECT t.topicId FROM Test t WHERE t.id = :testId")
    Integer findTopicIdByTestId(@Param("testId") Integer testId);

    Optional<Test> findByTopicId(Integer topicId);


}
