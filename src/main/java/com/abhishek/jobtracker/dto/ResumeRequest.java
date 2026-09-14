package com.abhishek.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResumeRequest {

    @NotBlank(message = "Resume label is required")
    private String label;

    private String originalFileName;

    @Size(max = 1000, message = "Resume URL is too long")
    private String externalUrl;
}