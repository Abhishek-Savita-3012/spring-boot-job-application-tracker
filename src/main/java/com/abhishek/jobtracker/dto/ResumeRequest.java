package com.abhishek.jobtracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

@Getter
@Setter
public class ResumeRequest {

    @NotBlank(message = "Resume label is required")
    @Size(max = 100, message = "Resume label cannot exceed 100 characters")
    private String label;

    @Size(max = 255, message = "File name cannot exceed 255 characters")
    private String originalFileName;

    @URL(message = "External resume URL must be valid")
    @Size(max = 1000, message = "External URL cannot exceed 1000 characters")
    private String externalUrl;
}