package com.ceres.project.models.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_topic_progress_association", uniqueConstraints = {
        @UniqueConstraint(columnNames = {"user_id", "course_topic_id"})
})
public class UserTopicProgress {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "course_topic_id", nullable = false)
    private Integer courseTopicId;

    @Column(name = "progress_percentage", nullable = false)
    private double progressPercentage = 0.0;

    @Column(name = "completed_at")
    private LocalDateTime completedAt;

    @Column(name = "completed", nullable = false)
    private boolean completed = false;

}

