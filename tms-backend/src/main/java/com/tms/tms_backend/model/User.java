package com.tms.tms_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("users")
public class User {
    @Id
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String role;
    private String clientCode;
    private Long clientId;
    private LocalDateTime createdAt;

    public User(String firstName, String lastName, String email, String password, String role, String clientCode, Long clientId) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
        this.clientCode = clientCode;
        this.clientId = clientId;
        this.createdAt = LocalDateTime.now();
    }

    @Column("project_ids")
    private List<String> projectIds;
}
