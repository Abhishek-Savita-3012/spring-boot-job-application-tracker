package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApiResponse;
import com.abhishek.jobtracker.dto.FollowUpReminderResponse;
import com.abhishek.jobtracker.dto.InterviewRoundRequest;
import com.abhishek.jobtracker.dto.InterviewRoundResponse;
import com.abhishek.jobtracker.service.InterviewRoundService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Interview Rounds",
        description = "Manage interview rounds for job applications"
)
@RestController
@RequestMapping("/api/applications/{applicationId}/interviews")
public class InterviewRoundController {

    private final InterviewRoundService interviewRoundService;

    public InterviewRoundController(InterviewRoundService interviewRoundService) {
        this.interviewRoundService = interviewRoundService;
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Interview Round created successfully"
            )
    })
    @PostMapping
    public ResponseEntity<ApiResponse<InterviewRoundResponse>>
    createInterviewRound(@PathVariable Long applicationId, @Valid @RequestBody InterviewRoundRequest request) {

        InterviewRoundResponse response = interviewRoundService.createInterviewRound(applicationId, request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Interview Round created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<InterviewRoundResponse>>>
    getAllInterviewRounds(@PathVariable Long applicationId) {

        List<InterviewRoundResponse> response = interviewRoundService.getAllInterviewRounds(applicationId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Interview Rounds fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/{roundId}")
    public ResponseEntity<ApiResponse<InterviewRoundResponse>> getInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId) {

        InterviewRoundResponse response = interviewRoundService.getInterviewRound(applicationId, roundId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Interview Round fetched successfully",
                        response
                )
        );
    }

    @PutMapping("/{roundId}")
    public ResponseEntity<ApiResponse<InterviewRoundResponse>> updateInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId, @Valid @RequestBody InterviewRoundRequest request) {

        InterviewRoundResponse response = interviewRoundService.updateInterviewRound(applicationId, roundId, request);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Interview Round updated successfully",
                        response
                )
        );
    }

    @DeleteMapping("/{roundId}")
    public ResponseEntity<Void> deleteInterviewRound(@PathVariable Long applicationId, @PathVariable Long roundId) {

        interviewRoundService.deleteInterviewRound(applicationId, roundId);

        return ResponseEntity.noContent().build();
    }
}