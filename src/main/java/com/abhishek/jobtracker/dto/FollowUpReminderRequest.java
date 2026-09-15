package com.abhishek.jobtracker.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class FollowUpReminderRequest {

    @NotNull(message = "Reminder date and time is required")
    @Future(message = "Reminder must be scheduled in the future")
    private LocalDateTime remindAt;

    @NotBlank(message = "Reminder message is required")
    @Size(max = 1000, message = "Reminder message is too long")
    private String message;
}