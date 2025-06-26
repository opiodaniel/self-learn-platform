CREATE TABLE IF NOT EXISTS course_enrollment (
    id SERIAL PRIMARY KEY,
    enrollment_date TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    progress_percentage DOUBLE PRECISION DEFAULT 0,
    status VARCHAR(50) DEFAULT 'ACTIVE',
    course_id INTEGER NOT NULL REFERENCES courses(id),
    user_id VARCHAR(255) NOT NULL REFERENCES system_user(servspace_id),

    -- Prevent duplicate enrollments
    CONSTRAINT uq_user_course UNIQUE (user_id, course_id)
);
