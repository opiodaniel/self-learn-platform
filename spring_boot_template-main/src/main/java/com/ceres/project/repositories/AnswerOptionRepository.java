package com.ceres.project.repositories;


import com.ceres.project.models.database.AnswerOption;
import com.ceres.project.models.jpa_helpers.repository.JetRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepository extends JetRepository<AnswerOption, Long> {

    AnswerOption findFirstByQuestionIdAndIsCorrectTrue(Integer questionId);
    List<AnswerOption> findByQuestionId(Integer questionId);

    void deleteByQuestionId(Integer questionId);

}


