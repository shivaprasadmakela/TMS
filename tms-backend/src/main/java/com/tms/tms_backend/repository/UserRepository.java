package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.User;
import io.micrometer.observation.ObservationFilter;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface UserRepository extends ReactiveCrudRepository<User, String> {
    Mono<User> findByEmail(String email);

    Flux<User> findAllByClientCode(String clientCode);
}