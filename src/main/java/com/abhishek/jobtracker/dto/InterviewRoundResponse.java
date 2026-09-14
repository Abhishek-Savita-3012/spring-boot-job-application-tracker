package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.InterviewOutcome;
import com.abhishek.jobtracker.entity.InterviewRoundType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class InterviewRoundResponse {

    private Long id;
    private Integer roundNumber;
    private String title;
    private InterviewRoundType roundType;

    private LocalDateTime scheduledAt;
    private String interviewer;

    private InterviewOutcome outcome;
    private String notes;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}