package com.abhishek.jobtracker.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ResumeResponse {

    private Long id;
    private String label;
    private String originalFileName;
    private String externalUrl;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}