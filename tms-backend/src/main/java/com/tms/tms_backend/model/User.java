package com.tms.tms_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@Table("users")
public class User {
    @Id
    private Long id;
    private String email;
    private String password;
    private String role;
    private LocalDateTime createdAt;

}
