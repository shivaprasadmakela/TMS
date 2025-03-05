package com.tms.tms_backend.controller;

import com.tms.tms_backend.dto.UserDTO;
import com.tms.tms_backend.model.User;
import com.tms.tms_backend.service.UserService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
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

    @PostMapping("/create")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<UserDTO> createUser(@RequestBody User user) {
        return userService.register(user);
    }

    @GetMapping()
    @PreAuthorize("hasAnyRole('ADMIN', 'MANAGER')")
    public Flux<UserDTO> getAllUsers(@RequestParam String clientCode) {
        return userService.getAllUsers(clientCode);
    }

    @PutMapping("/api/users/{id}/update")  // ✅ More specific path
    public Mono<UserDTO> updateUser(@PathVariable Long id, @RequestBody User user) {
        return userService.updateUser(id, user);
    }

    @GetMapping("/{id}")
    public Mono<UserDTO> getUserById(@PathVariable Long id) {
        return userService.getUserById(Long.valueOf(String.valueOf(id)));
    }

    @DeleteMapping("/delete/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public Mono<Void> deleteUser(@PathVariable Long id) {
        return userService.deleteUser(id).then();
    }

    @GetMapping("/profile")
    @PreAuthorize("hasRole('USER')")
    public Mono<ResponseEntity<UserDTO>> getProfile() {
        return ReactiveSecurityContextHolder.getContext()
                .map(context -> (String) context.getAuthentication().getPrincipal())
                .flatMap(userService::findByEmail)
                .map(ResponseEntity::ok)
                .defaultIfEmpty(ResponseEntity.notFound().build());
    }
}
