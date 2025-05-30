package com.e_learning.repository;

import com.e_learning.Enum.VoteType;
import com.e_learning.model.CourseVote;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface CourseVoteRepository extends JpaRepository<CourseVote, Long> {
    Optional<CourseVote> findByUserIdAndCourseId(Long userId, Long courseId);
    long countByCourseIdAndVoteType(Long courseId, VoteType voteType);

    @Query("SELECT COUNT(v) FROM CourseVote v WHERE v.course.id = :courseId AND v.voteType = 'UPVOTE'")
    long countUpvotesByCourseId(Long courseId);

    @Query("SELECT COUNT(v) FROM CourseVote v WHERE v.course.id = :courseId AND v.voteType = 'DOWNVOTE'")
    long countDownvotesByCourseId(Long courseId);

}

