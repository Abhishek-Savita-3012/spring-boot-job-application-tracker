package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ReminderStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class FollowUpReminderResponse {

    private Long id;

    private LocalDateTime remindAt;

    private String message;

    private ReminderStatus status;

    private LocalDateTime notifiedAt;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}