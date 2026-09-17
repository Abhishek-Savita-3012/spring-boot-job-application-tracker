package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull(message = "Application status is required")
    private ApplicationStatus status;
}