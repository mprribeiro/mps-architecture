package com.mps.course.dtos;

import lombok.Getter;

import java.util.UUID;

public class NotificationCommandDto {

    private String title;
    private String message;
    @Getter
    private UUID userId;

    public void setTitle(String title) {
        this.title = title;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
    }
}
