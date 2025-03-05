package com.tms.tms_backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponseDTO {
    private String email;
    private String firstNme;
    private String lastName;
    private String role;
    private String clientCode;
    private Long clientId;
    private String token;
    private Long expiresAt;
}
