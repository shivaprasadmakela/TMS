package com.tms.tms_backend.controller;

import com.tms.tms_backend.model.Task;
import com.tms.tms_backend.service.TaskService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tasks")
public class TaskController {
    private final TaskService taskService;

    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Mono<ResponseEntity<Task>> createTask(@RequestBody Task task) {
        return taskService.createTask(task)
                .map(ResponseEntity::ok);
    }

    @GetMapping("/project/{projectId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    public Flux<Task> getTasksByProject(@PathVariable String projectId) {
        return taskService.getTasksByProject(projectId);
    }

    @GetMapping("/assignee/{userId}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    public Flux<Task> getTasksByAssignee(@PathVariable String userId) {
        return taskService.getTasksByAssignee(userId);
    }

    @PutMapping("/{taskId}/update")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Mono<ResponseEntity<Task>> updateTask(@PathVariable String taskId, @RequestBody Task task) {
        return taskService.updateTask(taskId, task)
                .map(ResponseEntity::ok);
    }

    @PutMapping("/{taskId}/status")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    public Mono<ResponseEntity<Task>> updateTaskStatus(@PathVariable String taskId, @RequestParam String status) {
        return taskService.updateTaskStatus(taskId, status)
                .map(ResponseEntity::ok);
    }

    @PostMapping("/{taskId}/comment")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    public Mono<ResponseEntity<Task>> addCommentToTask(@PathVariable String taskId, @RequestParam String userId, @RequestParam String message) {
        return taskService.addCommentToTask(taskId, userId, message)
                .map(ResponseEntity::ok);
    }

    @DeleteMapping("/{taskId}/delete")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Mono<ResponseEntity<Void>> deleteTask(@PathVariable String taskId) {
        return taskService.deleteTask(taskId)
                .thenReturn(ResponseEntity.ok().build());
    }
}
