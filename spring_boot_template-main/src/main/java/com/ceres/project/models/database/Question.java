package com.ceres.project.models.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name="course_question")
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    @Basic
    @Column(name = "course_test_id", nullable = false)
    private Integer testId;

    @Basic
    @Column(name = "multiple_choice", nullable = false)
    private boolean multipleAnswersAllowed; // true = multiple correct answers allowed
}
