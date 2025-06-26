package com.ceres.project.services;

import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.services.auth.AuthService;
import com.ceres.project.utils.OperationReturnObject;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebActionsService {

    private final AuthService authService;

    // Elearning service
    private final CourseService courseService;
    private final TopicService topicService;
    private final SubTopicService  subTopicService;
//    private final TestService testService;
//    private final SubmitTestService submitTestService;
    private final EnrollmentService enrollmentService;
//    private final CourseCommentService courseCommentService;
//    private final CourseVoteService courseVoteService;

    public OperationReturnObject processAction(String service, String action, JSONObject payload) {
        return switch (service) {
            case "Auth" -> authService.process(action, payload);

            // Elearning service endpoint
            case "Course" -> courseService.process(action,payload);
            case "Topic" -> topicService.process(action,payload);
            case "SubTopic" -> subTopicService.process(action,payload);
//            case "Test" -> testService.process(action,payload);
//            case "SubmitTest" -> submitTestService.process(action,payload);
            case "Enrollment" -> enrollmentService.process(action,payload);
//            case "Comment" -> courseCommentService.process(action,payload);
//            case "Vote" -> courseVoteService.process(action,payload);

            default -> {
                OperationReturnObject res = new OperationReturnObject();
                res.setReturnCodeAndReturnMessage(404, "UNKNOWN SERVICE");
                yield res;
            }
        };
    }
}
