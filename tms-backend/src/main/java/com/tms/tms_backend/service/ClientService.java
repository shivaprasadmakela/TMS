package com.tms.tms_backend.service;

import com.tms.tms_backend.model.Client;
import com.tms.tms_backend.repository.ClientRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class ClientService {

    private final ClientRepository clientRepository;

    public ClientService(ClientRepository clientRepository) {
        this.clientRepository = clientRepository;
    }

    public Mono<Client> createClient(Client client) {
        return clientRepository.save(client);
    }

    public Mono<Client> getClientById(Long id) {
        return clientRepository.findById(id);
    }

    public Mono<Client> getClientByCode(String code) {
        return clientRepository.findByCode(code);
    }

    public Flux<Client> getAllClients() {
        return clientRepository.findAll();
    }

    public Mono<Client> updateClient(Long id, Client client) {
        return clientRepository.findById(id)
                .flatMap(existingClient -> {
                    existingClient.setCode(client.getCode());
                    // Add other fields as they are added to the model
                    return clientRepository.save(existingClient);
                });
    }

    public Mono<Void> deleteClient(Long id) {
        return clientRepository.deleteById(id);
    }
}
