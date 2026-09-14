package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateStatusRequest {

    @NotNull(message = "Status is required")
    private ApplicationStatus status;
}