package com.tms.tms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String role;
    private LocalDateTime createdAt;
}
