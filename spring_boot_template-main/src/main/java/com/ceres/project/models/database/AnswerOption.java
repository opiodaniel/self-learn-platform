package com.ceres.project.models.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="course_answer")
public class AnswerOption {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "answer_text", nullable = false)
    private String answerText;

    @Basic
    @Column(name = "is_correct", nullable = false)
    private boolean isCorrect;

    @Basic
    @Column(name = "course_question_id", nullable = false)
    private Integer questionId;
}
