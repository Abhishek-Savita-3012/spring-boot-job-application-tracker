package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ResumeRequest;
import com.abhishek.jobtracker.dto.ResumeResponse;
import com.abhishek.jobtracker.service.ResumeService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/resumes")
public class ResumeController {

    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping
    public ResponseEntity<ResumeResponse> createResume(@Valid @RequestBody ResumeRequest request) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        resumeService.createResume(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<ResumeResponse>> getAllResumes() {

        return ResponseEntity.ok(
                resumeService.getAllResumes()
        );
    }

    @GetMapping("/{resumeId}")
    public ResponseEntity<ResumeResponse> getResume(@PathVariable Long resumeId) {

        return ResponseEntity.ok(
                resumeService.getResume(resumeId)
        );
    }
}