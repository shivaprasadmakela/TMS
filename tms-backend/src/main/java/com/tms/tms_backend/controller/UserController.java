package com.tms.tms_backend.controller;

import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.service.UserService;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/users")
public class UserController {
    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping()
    public Mono<User> registerUser(@RequestBody User user) {
        return userService.registerUser(user);
    }

    @GetMapping
    public Flux<UserDTO> getAllUsers() {
        return userService.getAllUsers();
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(String.valueOf(id));
    }

    @DeleteMapping("/{id}")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id).then();
    }
}
