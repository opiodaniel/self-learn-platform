package com.ceres.project.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.ceres.project.models.database.Course;
import com.ceres.project.models.database.Subtopic;
import com.ceres.project.models.database.Topic;
import com.ceres.project.repositories.CourseRepository;
import com.ceres.project.repositories.SubtopicRepository;
import com.ceres.project.repositories.TopicRepository;
import com.ceres.project.services.base.BaseWebActionsService;
import com.ceres.project.utils.BadRequest400;
import com.ceres.project.utils.OperationReturnObject;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService extends BaseWebActionsService {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;
    private final SubtopicRepository subtopicRepository;

    // Create Topic
    public OperationReturnObject createTopic(JSONObject request) {

        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            // Validate structure
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long courseId = data.getLong("courseId");
            String title = data.getString("title");
            String description = data.getString("description");

            Course course = courseRepository.findById(courseId)
                    .orElseThrow(() -> new BadRequest400("Course ID not found"));

            // Build Topic
            Topic topic = new Topic();
            topic.setTitle(title);
            topic.setDescription(description);
            topic.setCourseId(course.getId().intValue());

            // Save Topic
            topicRepository.save(topic);

            returnObj.setReturnObject(topic);
            returnObj.setReturnCodeAndReturnMessage(200, "Topic created successfully.");
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject getCourseTopics(JSONObject request) {
        requiresAuth();
        OperationReturnObject returnObj = new OperationReturnObject();
        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");
            Long courseId = data.getLong("courseId");

            List<Topic> topics = topicRepository.findByCourseId(courseId);

            // Add topics as a JSON array
            JSONArray topicsArray = new JSONArray();
            for (Topic topic : topics) {
                JSONObject topicJson = new JSONObject();
                topicJson.put("id", topic.getId());
                topicJson.put("title", topic.getTitle());
                topicJson.put("description", topic.getDescription());

                // Fetch subtopics for each topic
                List<Subtopic> subtopics = subtopicRepository.findByTopicId(topic.getId());
                JSONArray subtopicsArray = new JSONArray();

                ObjectMapper mapper = new ObjectMapper();
                List<Map<String, Object>> responseList = new ArrayList<>();

                for (Subtopic subtopic : subtopics) {
                    JSONObject subtopicJson = new JSONObject();
                    subtopicJson.put("id", subtopic.getId());
                    subtopicJson.put("title", subtopic.getTitle());
                    JsonNode contentJson = mapper.readTree(subtopic.getContent());
                    subtopicJson.put("content", contentJson);
                    subtopicsArray.add(subtopicJson);
                }

                topicJson.put("subtopics", subtopicsArray); // Add subtopics to topic
                topicsArray.add(topicJson);
            }

            if(topics.isEmpty()){
                returnObj.setCodeAndMessageAndReturnObject(200, "No Course Topics", topicsArray);
                return returnObj;
            }

            returnObj.setCodeAndMessageAndReturnObject(200, "Course topics retrieved successfully", topicsArray);
            return returnObj;

        }catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject updateTopic(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long topicId = data.getLong("topicId");
            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new BadRequest400("Topic ID not found"));

            // Update fields only if they exist in the request
            if (data.containsKey("title")) {
                topic.setTitle(data.getString("title"));
            }

            if (data.containsKey("description")) {
                topic.setDescription(data.getString("description"));
            }

            topicRepository.save(topic);

            returnObj.setReturnObject(topic);
            returnObj.setReturnCodeAndReturnMessage(200, "Topic updated successfully.");
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    public OperationReturnObject deleteTopic(JSONObject request) {
        requiresAuth();

        OperationReturnObject returnObj = new OperationReturnObject();

        try {
            requires("data", request);
            JSONObject data = request.getJSONObject("data");

            Long topicId = data.getLong("topicId");

            Topic topic = topicRepository.findById(topicId)
                    .orElseThrow(() -> new BadRequest400("Topic ID not found"));

            topicRepository.delete(topic);

            returnObj.setCodeAndMessageAndReturnObject(200, "Topic deleted successfully.", topic);
            return returnObj;

        } catch (Exception e) {
            returnObj.setReturnCodeAndReturnMessage(400, e.getMessage());
            return returnObj;
        }
    }

    @Override
    public OperationReturnObject switchActions(String action, JSONObject request) {
        return switch (action) {
            case "createTopic" -> createTopic(request);
            case "getCourseTopics" -> getCourseTopics(request);
            case "updateTopic" -> updateTopic(request);
            case "deleteTopic" -> deleteTopic(request);
            default -> throw new IllegalArgumentException("Unknown action: " + action);
        };
    }
}
