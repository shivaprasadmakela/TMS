package com.tms.tms_backend.controller;

import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("api/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public Mono<ResponseEntity<String>> registerUser(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping
    public Flux<UserDTO> getAllUsers(@RequestParam String clientCode) {
        return userService.getAllUsers(clientCode);
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(Long.valueOf(String.valueOf(id)));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id).then();
    }
}
