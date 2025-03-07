package com.tms.tms_backend.controller;

import com.tms.tms_backend.model.Project;
import com.tms.tms_backend.service.ProjectService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

@RestController
@RequestMapping("/api/projects")
public class ProjectController {

    private final ProjectService projectService;

    public ProjectController(ProjectService projectService) {
        this.projectService = projectService;
    }

    @PostMapping("/create")
    @PreAuthorize("hasAnyRole('MANAGER', 'ADMIN')")
    public Mono<Project> createProject(@RequestBody Project project) {
        return projectService.createProject(project);
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER', 'USER')")
    public Mono<ResponseEntity<Project>> getProjectById(@PathVariable String id) {
        return projectService.getProjectById(id)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Mono<ResponseEntity<List<Project>>> getAllProjects() {
        return projectService.getAllProjects()
                .collectList()
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }

    @GetMapping("/manager/{managerId}")
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Flux<Project> getProjectsByManager(@PathVariable String managerId) {
        return projectService.getProjectsByManager(managerId);
    }

    @PutMapping("/{id}/update")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Mono<Project> updateProject(@PathVariable String id, @RequestBody Project project) {
        return projectService.updateProject(id, project);
    }

    @DeleteMapping("/{id}/delete")
    @PreAuthorize("hasAnyRole('ADMIN','MANAGER')")
    public Mono<Void> deleteProject(@PathVariable String id) {
        return projectService.deleteProject(id);
    }
}
