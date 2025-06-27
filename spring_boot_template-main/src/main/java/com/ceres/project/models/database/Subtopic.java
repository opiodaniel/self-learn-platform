package com.ceres.project.models.database;

import jakarta.persistence.*;
import lombok.*;


@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@ToString
@Table(name = "course_subtopic")
public class Subtopic {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    private String title;

    @Column(columnDefinition = "TEXT")
    private String content;

    @Basic
    @Column(name = "topic_id", nullable = false)
    private Integer topicId;
}

