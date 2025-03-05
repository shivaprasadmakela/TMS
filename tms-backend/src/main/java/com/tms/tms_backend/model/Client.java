package com.tms.tms_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Table("clients")
public class Client {
    @Id
    private Long id;
    private String code;
    private LocalDateTime createdAt;

    public Client(String code) {
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }
}
