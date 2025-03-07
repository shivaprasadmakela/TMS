package com.tms.tms_backend.service;

import com.tms.tms_backend.dto.LoginResponse;
import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.Client;
import com.tms.tms_backend.model.Project;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.repository.ClientRepository;
import com.tms.tms_backend.repository.UserRepository;
import com.tms.tms_backend.repository.ProjectRepository;
import com.tms.tms_backend.util.ClientUtil;
import com.tms.tms_backend.util.JwtUtil;
import com.tms.tms_backend.util.UserUtil;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final ClientRepository clientRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final ProjectRepository projectRepository;

    public UserService(UserRepository userRepository, ClientRepository clientRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, ProjectRepository projectRepository) {
        this.userRepository = userRepository;
        this.clientRepository = clientRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.projectRepository = projectRepository;
    }

    public Mono<UserDTO> register(User user) {
        return Mono.just(user)
                .flatMap(u -> {
                    String missingField = UserUtil.validateFields(u, "firstName", "email", "password", "role", "clientCode", "clientId");
                    return missingField != null
                            ? Mono.error(new IllegalArgumentException("Missing field: " + missingField))
                            : Mono.just(u);
                })
                .flatMap(u -> userRepository.findByEmail(u.getEmail())
                        .flatMap(existingUser -> Mono.error(new IllegalArgumentException("User Already Exists")))
                        .switchIfEmpty(Mono.defer(() -> {
                            u.setPassword(passwordEncoder.encode(u.getPassword()));
                            return userRepository.save(u).map(this::convertToDTO);
                        })))
                .cast(UserDTO.class)
                .onErrorResume(e -> Mono.error(new RuntimeException("Registration failed: " + e.getMessage())));
    }


    public Mono<UserDTO> registerAdmin(User request) {
        return userRepository.findByEmail(request.getEmail())
                .flatMap(existingUser -> Mono.error(new IllegalArgumentException("User Already Exists")))
                .switchIfEmpty(ClientUtil.generateUniqueCode(request.getEmail(), clientRepository)
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
                                                .map(this::convertToDTO)
                                                .cast(UserDTO.class);
                                    });
                        }))
                .timeout(Duration.ofSeconds(10))
                .onErrorResume(e -> Mono.error(new RuntimeException("Admin registration failed: " + e.getMessage())))
                .cast(UserDTO.class);
    }



    public Mono<LoginResponse> login(User user) {
        return userRepository.findByEmail(user.getEmail())
                .filter(foundUser -> passwordEncoder.matches(user.getPassword(), foundUser.getPassword()))
                .map(foundUser -> {
                    long expirationTime = System.currentTimeMillis() + JwtUtil.getExpirationTime(); // Use constant
                    String token = jwtUtil.generateToken(foundUser.getEmail(), foundUser.getRole(), foundUser.getClientCode());
                    return new LoginResponse(convertToDTO(foundUser), token, expirationTime);
                })
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }


    public Flux<UserDTO> getAllUsers(String clientCode) {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> context.getAuthentication().getName())
                .flatMapMany(email -> userRepository.findAllByClientCode(clientCode))
                .map(this::convertToDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }


    public Mono<UserDTO> getUserById(Long id) {
        return userRepository.findById(String.valueOf(id))
                .map(this::convertToDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
    }


    public Mono<UserDTO> updateUser(Long id, User user) {
        return userRepository.findById(String.valueOf(id))
                .flatMap(existingUser -> {
                    existingUser.setFirstName(user.getFirstName());
                    existingUser.setLastName(user.getLastName());
                    existingUser.setEmail(user.getEmail());
                    existingUser.setRole(user.getRole());

                    return userRepository.save(existingUser);
                })
                .map(this::convertToDTO);
    }


    public Mono<Boolean> deleteUser(Long id) {
        return userRepository.findById(String.valueOf(id))
                .flatMap(existingUser -> userRepository.delete(existingUser).thenReturn(true))
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")))
                .onErrorResume(e -> Mono.error(new RuntimeException("Delete failed: " + e.getMessage())));
    }


    public Mono<UserDTO> findByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO)
                .switchIfEmpty(Mono.error(new RuntimeException("User not found")));
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
                user.getCreatedAt(),
                user.getProjectIds()
        );
    }

    public Mono<User> addProjectToUser(String userId, String projectId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    if (user.getProjectIds() == null) {
                        user.setProjectIds(new ArrayList<>());
                    }
                    if (!user.getProjectIds().contains(projectId)) {
                        user.getProjectIds().add(projectId);
                        return userRepository.save(user);
                    }
                    return Mono.just(user);
                });
    }

    public Mono<List<Project>> getAllProjectsForUser(String userId) {
        return userRepository.findById(userId)
                .flatMap(user -> {
                    List<String> projectIds = user.getProjectIds();
                    if (projectIds == null || projectIds.isEmpty()) {
                        return Mono.just(List.of());
                    }
                    return projectRepository.findAllById(projectIds)
                            .collectList()
                            .map(projects -> projects.stream()
                                    .map(project -> new Project(project.getId(), project.getName(), project.getDescription(), project.getManagerId()))
                                    .collect(Collectors.toList()));
                });
    }

}
