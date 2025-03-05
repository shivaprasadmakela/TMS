package com.tms.tms_backend.service;

import com.tms.tms_backend.dto.LoginResponseDTO;
import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.Client;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.repository.ClientRepository;
import com.tms.tms_backend.repository.UserRepository;
import com.tms.tms_backend.util.ClientUtil;
import com.tms.tms_backend.util.JwtUtil;
import com.tms.tms_backend.util.UserUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public UserService(UserRepository userRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;

    }

    public Mono<ResponseEntity<String>> register(User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    String missingField = UserUtil.validateFields(u, "firstName", "email", "password", "role","clientCode","clientId" );
                    return missingField != null ?
                            Mono.error(new IllegalArgumentException("Missing field: " + missingField)) :
                            Mono.just(u);
                })
                .flatMap(u -> userRepository.findByEmail(u.getEmail())
                        .flatMap(existingUser -> Mono.just(ResponseEntity.badRequest().body("User Already Exists")))
                        .switchIfEmpty(Mono.defer(() -> {
                            u.setPassword(passwordEncoder.encode(u.getPassword()));
                            return userRepository.save(u)
                                    .map(savedUser -> ResponseEntity.ok("User Successfully Registered"));
                        })))
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(e.getMessage())));
    }

    public Mono<ResponseEntity<String>> registerAdmin(User request) {
        return userRepository.findByEmail(request.getEmail())
                .flatMap(existingUser -> Mono.just(ResponseEntity.badRequest().body("User Already Exists")))
                .switchIfEmpty(
                        ClientUtil.generateUniqueCode(request.getEmail(), clientRepository)
                                .flatMap(clientCode -> {
                                    Client newClient = new Client(clientCode);
                                    return clientRepository.save(newClient)
                                            .flatMap(savedClient -> {
                                                User admin = new User(
                                                        request.getFirstName(),
                                                        request.getLastName(),
                                                        request.getEmail(),
                                                        passwordEncoder.encode(request.getPassword()),
                                                        "ADMIN",
                                                        savedClient.getCode(),
                                                        savedClient.getId()
                                                );
                                                return userRepository.save(admin)
                                                        .map(savedUser -> ResponseEntity.ok("Admin Successfully Registered"));
                                            });
                                })
                )
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.just(ResponseEntity.internalServerError().body("Unexpected error occurred!")));
    }

    public Mono<LoginResponseDTO> login(User user) {
        return userRepository.findByEmail(user.getEmail())
                .filter(foundUser -> passwordEncoder.matches(user.getPassword(), foundUser.getPassword()))
                .map(foundUser -> {
                    String token = jwtUtil.generateToken(foundUser.getEmail(), foundUser.getRole(), foundUser.getClientCode());
//                    Long expiresAt = jwtUtil.getExpirationDate(token).getTime();

                    return new LoginResponseDTO(
                            foundUser.getEmail(),
                            foundUser.getFirstName(),
                            foundUser.getLastName(),
                            foundUser.getRole(),
                            foundUser.getClientCode(),
                            foundUser.getClientId(),
                            token
//                            expiresAt
                    );
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }

    public Flux<UserDTO> getAllUsers(String clientCode) {
        return userRepository.findAllByClientCode(clientCode)
                .map(this::convertToDTO);
    }

    public Mono<UserDTO> getUserById(Long id) {
        return userRepository.findById(String.valueOf(id))
                .map(this::convertToDTO);
    }

    public Mono<Boolean> deleteUser(Long id) {
        return userRepository.deleteById(String.valueOf(id)).then(Mono.just(true));
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(
                user.getId(),
                user.getFirstName(),
                user.getLastName(),
                user.getEmail(),
                user.getRole(),
                user.getClientCode(),
                user.getClientId(),
                user.getCreatedAt()
        );
    }
}
