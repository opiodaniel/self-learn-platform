package com.e_learning.controller;

import com.e_learning.dto.CourseCommentRequest;
import com.e_learning.dto.CourseCommentResponse;
import com.e_learning.model.CourseComment;
import com.e_learning.service.CourseCommentService;
import com.e_learning.service.ResponseService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/comment")
public class CourseCommentController {

    private final CourseCommentService commentService;
    private final ResponseService responseService;

    public CourseCommentController(CourseCommentService commentService,
                                   ResponseService responseService) {
        this.commentService = commentService;
        this.responseService = responseService;
    }

    @PostMapping("/on/course/{courseId}")
    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Long courseId,
                                                          @Valid @RequestBody CourseCommentRequest commentRequest,
                                                          Authentication authentication) {
        try {
            String username = authentication.getName();
            CourseComment saved = commentService.addComment(courseId, username, commentRequest.getContent());
            return responseService.createSuccessResponse(201, saved, HttpStatus.CREATED);
        } catch (RuntimeException ex) {
            return responseService.createErrorResponse(400, Map.of("commentError", ex.getMessage()), HttpStatus.BAD_REQUEST);
        }
    }

//    @GetMapping("/{courseId}/comments")
//    public ResponseEntity<Map<String, Object>> getComments(@PathVariable Long courseId) {
//        List<CourseComment> comments = commentService.getCommentsByCourseId(courseId);
//        return responseService.createSuccessResponse(200, comments, HttpStatus.OK);
//    }

    @GetMapping("/{courseId}/comments")
    public ResponseEntity<Map<String, Object>> getComments(@PathVariable Long courseId) {
        List<CourseCommentResponse> comments = commentService.getCommentsByCourseId(courseId);
        return responseService.createSuccessResponse(200, comments, HttpStatus.OK);
    }


    @PutMapping("/update/{commentId}")
    public ResponseEntity<Map<String, Object>> updateComment(
            @PathVariable Long commentId,
            @RequestBody CourseCommentRequest request,
            Authentication authentication) {

        String username = authentication.getName();
        CourseComment updated = commentService.updateComment(commentId, request.getContent(), username);
        return responseService.createSuccessResponse(200, updated, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{commentId}")
    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId,
                                                             Authentication authentication) {
        String username = authentication.getName();
        commentService.deleteComment(commentId, username);
        return responseService.createSuccessResponse(200, "Comment deleted successfully", HttpStatus.OK);
    }
}


//@RestController
//@RequestMapping("/api/v1/comment")
//public class CourseCommentController {
//
//    private final CourseCommentService commentService;
//    private final ResponseService responseService;
//
//    public CourseCommentController(CourseCommentService commentService,
//                                   ResponseService responseService) {
//        this.commentService = commentService;
//        this.responseService = responseService;
//    }
//
//    @PostMapping("/on/course/{courseId}")
//    public ResponseEntity<Map<String, Object>> addComment(@PathVariable Long courseId,
//                                                          @Valid @RequestBody CourseComment comment,
//                                                          Authentication authentication) {
//        try {
//            String username = authentication.getName();
//            CourseComment saved = commentService.addComment(courseId, username, comment);
//            return responseService.createSuccessResponse(201, saved, HttpStatus.CREATED);
//        } catch (RuntimeException ex) {
//            return responseService.createErrorResponse(400, Map.of("commentError", ex.getMessage()), HttpStatus.BAD_REQUEST);
//        }
//    }
//
//    @GetMapping("/{courseId}/comments")
//    public ResponseEntity<Map<String, Object>> getComments(@PathVariable Long courseId) {
//        List<CourseComment> comments = commentService.getCommentsByCourseId(courseId);
//        return responseService.createSuccessResponse(200, comments, HttpStatus.OK);
//    }
//
//    @PutMapping("/update/{commentId}")
//    public ResponseEntity<Map<String, Object>> updateComment(
//            @PathVariable Long commentId,
//            @RequestBody Map<String, String> requestBody) {
//
//        String updatedText = requestBody.get("content");
//        CourseComment updated = commentService.updateComment(commentId, updatedText);
//        return responseService.createSuccessResponse(200, updated, HttpStatus.OK);
//    }
//
//    @DeleteMapping("/delete/{commentId}")
//    public ResponseEntity<Map<String, Object>> deleteComment(@PathVariable Long commentId) {
//        commentService.deleteComment(commentId);
//        return responseService.createSuccessResponse(200, "Comment deleted successfully", HttpStatus.OK);
//    }
//
//}

