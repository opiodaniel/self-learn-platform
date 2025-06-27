package com.ceres.project.services;

import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.Subtopic;
import com.ceres.project.models.database.Topic;
import com.ceres.project.repositories.CourseRepository;
import com.ceres.project.repositories.SubtopicRepository;
import com.ceres.project.repositories.TopicRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.BadRequest400;
import com.ceres.project.utils.OperationReturnObject;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class SubTopicService extends BaseWebActionsService {

    private final TopicRepository topicRepository;
    private final SubtopicRepository subtopicRepository;
    private final CourseRepository courseRepository;

    // Create Topic
    public OperationReturnObject createTopicSubTopic(JSONObject request) {

        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            // Validate structure
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long topicId = data.getLong("topicId");
            String title = data.getString("title");
            String content = data.getString("content");

            ObjectMapper mapper = new ObjectMapper();
            // Converts "content" object to actual JSON string
            String contentJson = mapper.writeValueAsString(content);

            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new BadRequest400("Course ID not found"));

            // Build SubTopic
            Subtopic subtopic = new Subtopic();
            subtopic.setTitle(title);
            subtopic.setContent(contentJson);
            subtopic.setTopicId(topic.getId().intValue());

            // Save SubTopic
            subtopicRepository.save(subtopic);

            returnObj.setReturnObject(subtopic);
            returnObj.setReturnCodeAndReturnMessage(200, "SubTopic created successfully.");
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject getSubTopicsByTopicId(JSONObject request) {

        requiresAuth();
        OperationReturnObject returnObj = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            Long topicId = data.getLong("topicId");

            List<Subtopic> subtopic = subtopicRepository.findByTopicId(topicId);

            if(subtopic.isEmpty()){
                returnObj.setCodeAndMessageAndReturnObject(200, "No SubTopics For this topic", subtopic);
                return returnObj;
            }

            returnObj.setCodeAndMessageAndReturnObject(200, "Topic subtopics retrieved successfully", subtopic);
            return returnObj;

        }catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject updateSubTopic(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long subtopicId = data.getLong("subtopicId");
            Subtopic subtopic = subtopicRepository.findById(subtopicId)
                    .orElseThrow(() -> new BadRequest400("SubTopic ID not found"));

            if (data.containsKey("title")) {
                subtopic.setTitle(data.getString("title"));
            }

            if (data.containsKey("content")) {
                ObjectMapper mapper = new ObjectMapper();
                String contentJson = mapper.writeValueAsString(data.get("content"));
                subtopic.setContent(contentJson);
            }

            subtopicRepository.save(subtopic);

            returnObj.setReturnObject(subtopic);
            returnObj.setReturnCodeAndReturnMessage(200, "SubTopic updated successfully.");
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject deleteSubTopic(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long subtopicId = data.getLong("subtopicId");

            Subtopic subtopic = subtopicRepository.findById(subtopicId)
                    .orElseThrow(() -> new BadRequest400("SubTopic ID not found"));

            subtopicRepository.delete(subtopic);

            returnObj.setCodeAndMessageAndReturnObject(200, "SubTopic deleted successfully.", subtopic);
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }




    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "createTopicSubTopic" -> createTopicSubTopic(request);
            case "getSubTopicsByTopicId"  -> getSubTopicsByTopicId(request);
            case "updateSubTopic"  -> updateSubTopic(request);
            case "deleteSubTopic"  -> deleteSubTopic(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
