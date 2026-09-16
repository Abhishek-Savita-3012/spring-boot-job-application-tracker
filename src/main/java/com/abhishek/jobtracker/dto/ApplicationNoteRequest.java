package com.abhishek.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ApplicationNoteRequest {

    @NotBlank(message = "Note content is required")
    @Size(max = 5000, message = "Note cannot exceed 5000 characters")
    private String content;
}