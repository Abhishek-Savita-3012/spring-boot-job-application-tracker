package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class StatusHistoryResponse {

    private Long id;

    private ApplicationStatus oldStatus;

    private ApplicationStatus newStatus;

    private LocalDateTime changedAt;
}