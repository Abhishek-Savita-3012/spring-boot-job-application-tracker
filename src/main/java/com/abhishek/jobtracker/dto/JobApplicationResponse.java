package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class JobApplicationResponse {

    private Long id;
    private String company;
    private String role;
    private String location;

    private EmploymentType employmentType;
    private WorkMode workMode;

    private BigDecimal salary;
    private String salaryCurrency;

    private String jobUrl;
    private String source;

    private ApplicationStatus status;

    private LocalDate appliedDate;
    private LocalDate deadline;

    private Long resumeId;
    private String resumeLabel;

    private String description;

    private boolean archived;
    private LocalDateTime archivedAt;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}