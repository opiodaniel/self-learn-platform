package com.ceres.project.repositories;


import com.ceres.project.models.database.UserTopicProgress;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;

import java.util.List;
import java.util.Optional;

public interface UserTopicProgressRepository extends JetRepository<UserTopicProgress, Long> {
    Optional<UserTopicProgress> findByUserIdAndCourseTopicId(String userId, Integer courseTopicId);

    List<UserTopicProgress> findByUserId(String userId);
    List<UserTopicProgress> findByCourseTopicId(Integer courseTopicId);


}

