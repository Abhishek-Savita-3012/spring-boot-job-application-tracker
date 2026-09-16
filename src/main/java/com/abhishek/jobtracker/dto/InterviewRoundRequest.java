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
    @Size(max = 150, message = "Interview title cannot exceed 150 characters")
    private String title;

    @NotNull(message = "Interview round type is required")
    private InterviewRoundType roundType;

    @NotNull(message = "Interview schedule is required")
    private LocalDateTime scheduledAt;

    private String interviewer;

    private InterviewOutcome outcome;

    @Size(max = 5000, message = "Interview notes cannot exceed 5000 characters")
    private String notes;
}