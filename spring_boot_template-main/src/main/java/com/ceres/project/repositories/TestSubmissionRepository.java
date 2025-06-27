package com.ceres.project.repositories;


import com.ceres.project.models.database.TestSubmission;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TestSubmissionRepository extends JetRepository<TestSubmission, String> {
    List<TestSubmission> findByUserId(String userId);

    TestSubmission findByUserIdAndTestId(String userId, Integer testId);

}

