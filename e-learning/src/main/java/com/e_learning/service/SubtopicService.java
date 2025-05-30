package com.e_learning.service;

import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Subtopic;
import com.e_learning.model.Topic;
import com.e_learning.repository.SubtopicRepository;
import com.e_learning.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SubtopicService {

    private final SubtopicRepository subtopicRepository;
    private final TopicRepository topicRepository;

    public SubtopicService(SubtopicRepository subtopicRepository, TopicRepository topicRepository) {
        this.subtopicRepository = subtopicRepository;
        this.topicRepository = topicRepository;
    }

    public Subtopic createSubtopic(Long topicId, Subtopic subtopic) {
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + topicId));

        subtopic.setTopic(topic);
        return subtopicRepository.save(subtopic);
    }

    public Optional<Subtopic> getSubTopicById(Long id) {
        return subtopicRepository.findById(id);
    }

    public List<Subtopic> getSubtopicsByTopicId(Long topicId) {
        return subtopicRepository.findByTopicId(topicId);
    }

    public Subtopic updateSubtopic(Long id, Subtopic updated) {
        Subtopic existing = subtopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtopic not found with id: " + id));

        if (updated.getTitle() != null) existing.setTitle(updated.getTitle());
        if (updated.getContent() != null) existing.setContent(updated.getContent());

        return subtopicRepository.save(existing);
    }

    public void deleteSubtopic(Long id) {
        Subtopic subtopic = subtopicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Subtopic not found with id: " + id));
        subtopicRepository.delete(subtopic);
    }
}

