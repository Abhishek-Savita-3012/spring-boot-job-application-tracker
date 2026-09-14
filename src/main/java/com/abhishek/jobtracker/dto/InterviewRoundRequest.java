package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.InterviewOutcome;
import com.abhishek.jobtracker.entity.InterviewRoundType;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class InterviewRoundRequest {

    @NotNull(message = "Round number is required")
    @Min(value = 1, message = "Round number must be at least 1")
    private Integer roundNumber;

    @NotBlank(message = "Interview title is required")
    private String title;

    @NotNull(message = "Interview round type is required")
    private InterviewRoundType roundType;

    private LocalDateTime scheduledAt;

    private String interviewer;

    private InterviewOutcome outcome;

    @Size(max = 5000, message = "Notes are too long")
    private String notes;
}