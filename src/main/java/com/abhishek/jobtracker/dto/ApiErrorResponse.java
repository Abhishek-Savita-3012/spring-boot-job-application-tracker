package com.abhishek.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.Map;

@Getter
@AllArgsConstructor
public class ApiErrorResponse {

    private boolean success;

    private int status;

    private String message;

    private Map<String, String> errors;

    private String path;

    private LocalDateTime timestamp;
}