package com.abhishek.job_tracker.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class JobController {
    @GetMapping("/api/health")
    public String health() {
        return "status: Ok" + "\n" + "application: Job Tracker Application";
    }
}
