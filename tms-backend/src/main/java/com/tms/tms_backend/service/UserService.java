package com.tms.tms_backend.service;

import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.repository.UserRepository;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
    }

    public Mono<User> registerUser(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }

    public Mono<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Mono<User> saveUser(User user) {
        return userRepository.save(user);
    }

    public Flux<UserDTO> getAllUsers() {
        return userRepository.findAll()
                .map(this::convertToDTO);
    }

    public Mono<UserDTO> getUserById(String id) {
        return userRepository.findById(id)
                .map(this::convertToDTO);
    }

    public Mono<Boolean> deleteUser(Long id) {
        return userRepository.deleteById(String.valueOf(id)).hasElement();
    }

    private UserDTO convertToDTO(User user) {
        return new UserDTO(user.getId(), user.getEmail(), user.getRole(), user.getCreatedAt());
    }
}
