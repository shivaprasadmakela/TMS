package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.Project;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveMongoRepository<Project, String> {
    Flux<Project> findByManagerId(String managerId); // Get projects by manager
}
