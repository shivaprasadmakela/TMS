package com.tms.tms_backend.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Table;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor // ✅ Needed for frameworks
@AllArgsConstructor // ✅ Constructor for initialization
@Table("clients")
public class Client {
    @Id
    private Long id;
    private String code;
    private LocalDateTime createdAt;

    // ✅ Constructor for easy creation
    public Client(String code) {
        this.code = code;
        this.createdAt = LocalDateTime.now();
    }
}
