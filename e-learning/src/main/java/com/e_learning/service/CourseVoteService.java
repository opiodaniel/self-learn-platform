package com.e_learning.service;

import com.e_learning.Enum.VoteType;
import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Course;
import com.e_learning.model.CourseVote;
import com.e_learning.model.User;
import com.e_learning.repository.CourseRepository;
import com.e_learning.repository.CourseVoteRepository;
import com.e_learning.repository.UserRepository;
import org.springframework.stereotype.Service;

@Service
public class CourseVoteService {

    private final CourseRepository courseRepository;
    private final UserRepository userRepository;
    private final CourseVoteRepository voteRepository;

    public CourseVoteService(CourseRepository courseRepository, UserRepository userRepository, CourseVoteRepository voteRepository) {
        this.courseRepository = courseRepository;
        this.userRepository = userRepository;
        this.voteRepository = voteRepository;
    }

    public CourseVote vote(Long courseId, String username, VoteType voteType) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found"));

        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        CourseVote vote = voteRepository.findByUserIdAndCourseId(user.getId(), courseId)
                .orElse(new CourseVote());

        vote.setCourse(course);
        vote.setUser(user);
        vote.setVoteType(voteType);

        return voteRepository.save(vote);
    }


    public long countVotes(Long courseId, VoteType voteType) {
        return voteRepository.countByCourseIdAndVoteType(courseId, voteType);
    }
}
