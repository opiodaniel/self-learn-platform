package com.ceres.project.models.database;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "course_enrollment")
public class Enrollment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Basic
    @Column(name = "user_id")
    private String userId;

    @Basic
    @Column(name = "course_id", nullable = false)
    private Integer courseId;

    @Basic
    @Column(name = "enrollment_date", nullable = false)
    private LocalDate enrollmentDate;

    private String status;  // e.g., "ACTIVE", "COMPLETED", "DROPPED"

    private Double progressPercentage; // optional, like 75.0

}

