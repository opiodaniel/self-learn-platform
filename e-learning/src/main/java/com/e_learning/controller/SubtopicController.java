package com.e_learning.controller;

import com.e_learning.model.Subtopic;
import com.e_learning.model.Topic;
import com.e_learning.service.ResponseService;
import com.e_learning.service.SubtopicService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.validation.Valid;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import java.util.*;

@RestController
@RequestMapping("/api/v1/subtopics")   //http://localhost:8000/api/v1/subtopics/topic/1
public class SubtopicController {

    private final SubtopicService subtopicService;
    private final ResponseService responseService;

    public SubtopicController(SubtopicService subtopicService, ResponseService responseService) {
        this.subtopicService = subtopicService;
        this.responseService = responseService;
    }

//    @PostMapping("/topic/{topicId}")
//    public ResponseEntity<Map<String, Object>> createSubtopic(@PathVariable Long topicId, @RequestBody Subtopic subtopic) {
//        Subtopic created = subtopicService.createSubtopic(topicId, subtopic);
//        return responseService.createSuccessResponse(201, created, HttpStatus.CREATED);
//    }

    @PostMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> createSubtopic(
            @PathVariable Long topicId,
            @RequestBody Map<String, Object> payload) throws JsonProcessingException {

        String title = (String) payload.get("title");

        // Convert "content" object to actual JSON string
        ObjectMapper mapper = new ObjectMapper();
        String contentJson = mapper.writeValueAsString(payload.get("content"));

        Subtopic subtopic = new Subtopic();
        subtopic.setTitle(title);
        subtopic.setContent(contentJson);

        Subtopic created = subtopicService.createSubtopic(topicId, subtopic);
        return responseService.createSuccessResponse(201, created, HttpStatus.CREATED);
    }


    @GetMapping("/{subTopicId}")
    public ResponseEntity<Map<String, Object>> getTopicById(@PathVariable Long subTopicId) {
        Optional<Subtopic> subtopic = subtopicService.getSubTopicById(subTopicId);
        return responseService.createSuccessResponse(200, subtopic, HttpStatus.OK);
    }


//    @GetMapping("/topic/{topicId}")
//    public ResponseEntity<Map<String, Object>> getSubtopicsByTopic(@PathVariable Long topicId) {
//        List<Subtopic> subtopics = subtopicService.getSubtopicsByTopicId(topicId);
//        return responseService.createSuccessResponse(200, subtopics, HttpStatus.OK);
//    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<Map<String, Object>> getSubtopicsByTopic(@PathVariable Long topicId) throws JsonProcessingException {
        List<Subtopic> subtopics = subtopicService.getSubtopicsByTopicId(topicId);

        ObjectMapper mapper = new ObjectMapper();
        List<Map<String, Object>> responseList = new ArrayList<>();

        for (Subtopic subtopic : subtopics) {
            Map<String, Object> subtopicData = new HashMap<>();
            subtopicData.put("id", subtopic.getId());
            subtopicData.put("title", subtopic.getTitle());

            // Parse the stored JSON string into JsonNode
            JsonNode contentJson = mapper.readTree(subtopic.getContent());
            subtopicData.put("content", contentJson);

            responseList.add(subtopicData);
        }

        return responseService.createSuccessResponse(200, responseList, HttpStatus.OK);
    }



    @PutMapping("/update/{id}")
    public ResponseEntity<Map<String, Object>> updateSubtopic(@Valid @PathVariable Long id, @RequestBody Subtopic updated) {
        Subtopic subtopic = subtopicService.updateSubtopic(id, updated);
        return responseService.createSuccessResponse(200, subtopic, HttpStatus.OK);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<Map<String, Object>> deleteSubtopic(@PathVariable Long id) {
        subtopicService.deleteSubtopic(id);
        return responseService.createSuccessResponse(200, "Subtopic deleted successfully", HttpStatus.OK);
    }
}

