package com.tms.tms_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.List;

@Data
@Document(collection = "projects")
public class Project {
    @Id
    private String id;
    private String name;
    private String description;
    private String managerId;
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Project(String id, String name, String description, String managerId) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.managerId = managerId;
    }
}
