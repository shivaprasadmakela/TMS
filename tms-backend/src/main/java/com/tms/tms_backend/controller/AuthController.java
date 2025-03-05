package com.tms.tms_backend.controller;

import com.tms.tms_backend.dto.LoginResponseDTO;
import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.repository.UserRepository;
import com.tms.tms_backend.service.UserService;
import com.tms.tms_backend.util.JwtUtil;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserService userService;
    private final JwtUtil jwtUtil;

    public AuthController(UserRepository userRepository, PasswordEncoder passwordEncoder, JwtUtil jwtUtil, UserService userService) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
        this.userService = userService;
    }

    @PostMapping("/register-admin")
    public Mono<UserDTO> registerAdmin(@RequestBody User user) {
        return userService.registerAdmin(user);
    }

    @PostMapping("/login")
    public Mono<ResponseEntity<LoginResponseDTO>> login(@RequestBody User user) {
        return userService.login(user)
                .map(ResponseEntity::ok)
                .onErrorResume(e -> Mono.just(ResponseEntity.badRequest().body(null)));
    }
}
