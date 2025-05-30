package com.e_learning.repository;

import com.e_learning.model.AnswerOption;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AnswerOptionRepository extends JpaRepository<AnswerOption, Long> {
    List<AnswerOption> findByQuestionId(Long questionId);
    List<AnswerOption> findByQuestionIdAndIsCorrectTrue(Long questionId);
    boolean existsByQuestionIdAndIsCorrectTrue(Long questionId);
}


