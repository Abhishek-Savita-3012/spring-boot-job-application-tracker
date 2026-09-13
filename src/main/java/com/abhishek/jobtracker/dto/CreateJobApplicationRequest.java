package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateJobApplicationRequest {

    @NotBlank(message = "Company is required")
    private String company;

    @NotBlank(message = "Role is required")
    private String role;

    private String location;

    private EmploymentType employmentType;

    private WorkMode workMode;

    @PositiveOrZero(message = "Salary cannot be negative")
    private BigDecimal salary;

    private String salaryCurrency;

    @Size(max = 1000, message = "Job URL is too long")
    private String jobUrl;

    private String source;

    private ApplicationStatus status;

    private LocalDate appliedDate;

    private LocalDate deadline;

    @Size(max = 5000, message = "Description is too long")
    private String description;
}