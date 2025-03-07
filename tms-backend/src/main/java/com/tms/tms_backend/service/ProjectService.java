package com.tms.tms_backend.service;

import com.tms.tms_backend.model.Project;
import com.tms.tms_backend.repository.ProjectRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ProjectService {
    private final ProjectRepository projectRepository;

    public ProjectService(ProjectRepository projectRepository) {
        this.projectRepository = projectRepository;
    }

    public Mono<Project> createProject(Project project) {
        return projectRepository.save(project);
    }

    public Flux<Project> getAllProjects() {
        return projectRepository.findAll();
    }

    public Flux<Project> getProjectsByManager(String managerId) {
        return projectRepository.findByManagerId(managerId);
    }

    public Mono<Project> updateProject(String projectId, Project project) {
        return projectRepository.findById(projectId)
                .flatMap(existingProject -> {
                    existingProject.setName(project.getName());
                    existingProject.setDescription(project.getDescription());
                    return projectRepository.save(existingProject);
                });
    }

    public Mono<Void> deleteProject(String projectId) {
        return projectRepository.deleteById(projectId);
    }

    public Mono<Project> getProjectById(String id) {
        return projectRepository.findById(id);
    }
}
