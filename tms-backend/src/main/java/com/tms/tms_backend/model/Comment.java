package com.tms.tms_backend.model;

import lombok.Data;

import java.time.Instant;

@Data
public class Comment {
    private String userId;
    private String message;
    private Instant timestamp = Instant.now();
    private Instant createdAt = Instant.now();
    private Instant updatedAt = Instant.now();

    public Comment(String userId, String message, Instant now) {
        this.userId = userId;
        this.message = message;
        this.timestamp = now;
        this.createdAt = now;
        this.updatedAt = now;
    }
}
