package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {

    // Find organization by unique code
    Mono<Client> findByCode(String code);

    // Check if organization code exists (useful for validation)
    Mono<Boolean> existsByCode(String clientCode);


}
