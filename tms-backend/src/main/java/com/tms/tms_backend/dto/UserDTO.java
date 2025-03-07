package com.tms.tms_backend.dto;

import com.ongres.scram.common.bouncycastle.pbkdf2.Strings;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class UserDTO {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String role;
    private String clientCode;
    private Long clientId;
    private LocalDateTime createdAt;
    private List<String> projects;


}
