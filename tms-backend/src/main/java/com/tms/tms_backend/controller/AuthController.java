package com.tms.tms_backend.controller;

import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.repository.UserRepository;
import com.tms.tms_backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @PostMapping("/register")
    public Mono<ResponseEntity<String>> register(@RequestBody User user) {
        return userRepository.findByEmail(user.getEmail())
                .flatMap(existingUser -> Mono.just(ResponseEntity.badRequest().body("User Already Exists")))
                .switchIfEmpty(
                        Mono.defer(() -> {
                            user.setPassword(passwordEncoder.encode(user.getPassword()));
                            return userRepository.save(user)
                                    .map(savedUser -> ResponseEntity.ok("User Successfully Registered"));
                        })
                );
    }

    @PostMapping("/login")
    public Mono<String> login(@RequestBody User user) {
        return userRepository.findByEmail(user.getEmail())
                .filter(foundUser -> passwordEncoder.matches(user.getPassword(), foundUser.getPassword()))
                .map(foundUser -> jwtUtil.generateToken(foundUser.getEmail(), foundUser.getRole()))
                .switchIfEmpty(Mono.error(new RuntimeException("Invalid credentials")));
    }
}
