package com.e_learning.repository;

import com.e_learning.model.TestSubmission;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubmissionRepository extends JpaRepository<TestSubmission, Long> {
    List<TestSubmission> findByUserId(Long userId);
    List<TestSubmission> findByTestId(Long testId);
}

