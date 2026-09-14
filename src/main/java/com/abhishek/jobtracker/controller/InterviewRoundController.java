package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.InterviewRoundRequest;
import com.abhishek.jobtracker.dto.InterviewRoundResponse;
import com.abhishek.jobtracker.service.InterviewRoundService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/applications/{applicationId}/interviews")
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    public InterviewRoundController(InterviewRoundService interviewRoundService) {
        this.interviewRoundService = interviewRoundService;
    }

    @PostMapping
    public ResponseEntity<InterviewRoundResponse>
    createInterviewRound(@PathVariable Long applicationId, @Valid @RequestBody InterviewRoundRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        interviewRoundService.createInterviewRound(applicationId, request)
                );
    }

    @GetMapping
    public ResponseEntity<List<InterviewRoundResponse>>
    getAllInterviewRounds(@PathVariable Long applicationId) {

        return ResponseEntity.ok(
                interviewRoundService.getAllInterviewRounds(applicationId)
        );
    }

    @GetMapping("/{roundId}")
    public ResponseEntity<InterviewRoundResponse> getInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId) {

        return ResponseEntity.ok(
                interviewRoundService.getInterviewRound(applicationId, roundId)
        );
    }

    @PutMapping("/{roundId}")
    public ResponseEntity<InterviewRoundResponse> updateInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId, @Valid @RequestBody InterviewRoundRequest request) {

        return ResponseEntity.ok(
                interviewRoundService.updateInterviewRound(applicationId, roundId, request)
        );
    }

    @DeleteMapping("/{roundId}")
    public ResponseEntity<Void> deleteInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId) {

        interviewRoundService.deleteInterviewRound(applicationId, roundId);

        return ResponseEntity.noContent().build();
    }
}