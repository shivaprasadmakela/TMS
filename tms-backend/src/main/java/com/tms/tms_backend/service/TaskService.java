package com.tms.tms_backend.service;

import com.tms.tms_backend.model.Task;
import com.tms.tms_backend.model.Comment;

import com.tms.tms_backend.repository.TaskRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Instant;

@Service
public class TaskService {
    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    // ✅ Create a new task under a project
    public Mono<Task> createTask(Task task) {
        return taskRepository.save(task);
    }

    // ✅ Get all tasks in a project
    public Flux<Task> getTasksByProject(String projectId) {
        return taskRepository.findByProjectId(projectId);
    }

    // ✅ Get tasks assigned to a specific user
    public Flux<Task> getTasksByAssignee(String userId) {
        return taskRepository.findByAssignee(userId);
    }

    // ✅ Update task details (title, description, assignee)
    public Mono<Task> updateTask(String taskId, Task task) {
        return taskRepository.findById(taskId)
                .flatMap(existingTask -> {
                    existingTask.setTitle(task.getTitle());
                    existingTask.setDescription(task.getDescription());
                    existingTask.setAssignee(task.getAssignee());
                    return taskRepository.save(existingTask);
                });
    }

    // ✅ Update task status
    public Mono<Task> updateTaskStatus(String taskId, String status) {
        return taskRepository.findById(taskId)
                .flatMap(existingTask -> {
                    existingTask.setStatus(status);
                    return taskRepository.save(existingTask);
                });
    }

    // ✅ Add comment to task
    public Mono<Task> addCommentToTask(String taskId, String userId, String message) {
        return taskRepository.findById(taskId)
                .flatMap(existingTask -> {
                    existingTask.getComments().add(new Comment(userId, message, Instant.now()));
                    return taskRepository.save(existingTask);
                });
    }

    // ✅ Delete a task
    public Mono<Void> deleteTask(String taskId) {
        return taskRepository.deleteById(taskId);
    }
}
