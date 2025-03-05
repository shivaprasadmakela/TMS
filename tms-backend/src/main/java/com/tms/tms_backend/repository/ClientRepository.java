package com.tms.tms_backend.repository;

import com.tms.tms_backend.model.Client;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface ClientRepository extends ReactiveCrudRepository<Client, Long> {

    Mono<Client> findByCode(String code);

    Mono<Boolean> existsByCode(String clientCode);


}
