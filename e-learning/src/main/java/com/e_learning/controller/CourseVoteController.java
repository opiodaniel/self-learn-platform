package com.e_learning.controller;

import com.e_learning.Enum.VoteType;
import com.e_learning.model.CourseVote;
import com.e_learning.service.CourseVoteService;
import com.e_learning.service.ResponseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/v1/vote")
public class CourseVoteController {

    private final CourseVoteService voteService;
    private final ResponseService responseService;

    public CourseVoteController(CourseVoteService voteService, ResponseService responseService) {
        this.voteService = voteService;
        this.responseService = responseService;
    }

    @PostMapping("/course/{courseId}")
    public ResponseEntity<Map<String, Object>> voteCourse(@PathVariable Long courseId,
                                                          @RequestParam VoteType voteType,
                                                          Authentication authentication) {
        try {
            String username = authentication.getName(); // Get logged-in username
            CourseVote vote = voteService.vote(courseId, username, voteType);
            return responseService.createSuccessResponse(200, vote, HttpStatus.OK);
        } catch (RuntimeException ex) {
            return responseService.createErrorResponse(400, Map.of("voteError", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/{courseId}/vote-counts")
    public ResponseEntity<Map<String, Object>> getVoteCounts(@PathVariable Long courseId) {
        long upvotes = voteService.countVotes(courseId, VoteType.UPVOTE);
        long downvotes = voteService.countVotes(courseId, VoteType.DOWNVOTE);
        Map<String, Long> voteCounts = Map.of("upvotes", upvotes, "downvotes", downvotes);
        return responseService.createSuccessResponse(200, voteCounts, HttpStatus.OK);
    }
}

