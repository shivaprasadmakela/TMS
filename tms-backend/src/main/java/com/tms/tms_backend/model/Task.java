package com.tms.tms_backend.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Document(collection = "tasks")
public class Task {
    @Id
    private String id;
    private String projectId;
    private String title;
    private String description;
    private String assignee;
    private String status = "To Do"; // ["To Do", "In Progress", "Done"]
    private List<Comment> comments = new ArrayList<>();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();
}

