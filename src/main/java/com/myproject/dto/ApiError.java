package com.myproject.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class ApiError {
    private String title;
    private String message;
    private String errorType;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime timestamp;

    public ApiError(String title, String message, String errorType) {
        this.title = title;
        this.message = message;
        this.errorType = errorType;
        this.timestamp = LocalDateTime.now();
    }
}