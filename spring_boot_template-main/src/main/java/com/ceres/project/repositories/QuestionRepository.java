package com.ceres.project.repositories;

import com.ceres.project.models.database.Question;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestionRepository extends JetRepository<Question, Long> {
    List<Question> findByTestId(Integer testId);
}

