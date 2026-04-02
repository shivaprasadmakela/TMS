package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.Project;
import org.springframework.data.repository.reactive.ReactiveSortingRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;

public interface ProjectRepository extends ReactiveSortingRepository<Project, String>, ReactiveCrudRepository<Project, String> {
    Flux<Project> findByManagerId(String managerId); // Get projects by manager
    Flux<Project> findAllByClientCode(String clientCode);
}
