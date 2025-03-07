package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.Task;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface TaskRepository extends ReactiveMongoRepository<Task, String> {
    Flux<Task> findByProjectId(String projectId); // Get tasks by project
    Flux<Task> findByAssignee(String assignee); // Get tasks assigned to a user
}
