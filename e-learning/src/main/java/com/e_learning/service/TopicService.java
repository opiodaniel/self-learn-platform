package com.e_learning.service;

import com.e_learning.exception.ResourceNotFoundException;
import com.e_learning.model.Course;
import com.e_learning.model.Topic;
import com.e_learning.repository.CourseRepository;
import com.e_learning.repository.TopicRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TopicService {

    private final TopicRepository topicRepository;
    private final CourseRepository courseRepository;

    public TopicService(TopicRepository topicRepository, CourseRepository courseRepository) {
        this.topicRepository = topicRepository;
        this.courseRepository = courseRepository;
    }

    public Topic createTopic(Long courseId, Topic topic) {
        Course course = courseRepository.findById(courseId)
                .orElseThrow(() -> new ResourceNotFoundException("Course not found with id: " + courseId));
        topic.setCourse(course);
        return topicRepository.save(topic);
    }

    public Optional<Topic> getTopicById(Long id) {
        return topicRepository.findById(id);
    }


    public List<Topic> getAllTopics() {
        return topicRepository.findAll();
    }

    public List<Topic> getTopicsByCourse(Long courseId) {
        return topicRepository.findByCourseId(courseId);
    }

    public Topic updateTopic(Long id, Topic updatedTopic) {
        Topic existing = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));

        if (updatedTopic.getTitle() != null) {
            existing.setTitle(updatedTopic.getTitle());
        }
        if (updatedTopic.getDescription() != null) {
            existing.setDescription(updatedTopic.getDescription());
        }
        if (updatedTopic.getCourse() != null) {
            existing.setCourse(updatedTopic.getCourse());
        }
        return topicRepository.save(existing);
    }


    public void deleteTopic(Long id) {
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Topic not found with id: " + id));
        topicRepository.delete(topic);
    }

}

