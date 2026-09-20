package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApiResponse;
import com.abhishek.jobtracker.dto.ResumeRequest;
import com.abhishek.jobtracker.dto.ResumeResponse;
import com.abhishek.jobtracker.service.ResumeService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(
        name = "Resumes",
        description = "Manage resume references"
)
@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @io.swagger.v3.oas.annotations.responses.ApiResponses({

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "201",
                    description = "Resume created successfully"
            ),

            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "401",
                    description = "Authentication required"
            ),
    })
    @PostMapping
    public ResponseEntity<ApiResponse<ResumeResponse>> createResume(@Valid @RequestBody ResumeRequest request) {

        ResumeResponse response = resumeService.createResume(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        ApiResponse.success(
                                "Resume created successfully",
                                response
                        )
                );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ResumeResponse>>> getAllResumes() {

        List<ResumeResponse> response = resumeService.getAllResumes();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resumes fetched successfully",
                        response
                )
        );
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ApiResponse<ResumeResponse>> getResume(@PathVariable Long resumeId) {

        ResumeResponse response = resumeService.getResume(resumeId);

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Resume fetched successfully",
                        response
                )
        );
    }
}