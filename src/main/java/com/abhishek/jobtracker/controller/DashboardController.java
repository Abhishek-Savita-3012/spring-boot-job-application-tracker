package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.DashboardResponse;
import com.abhishek.jobtracker.service.DashboardService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<DashboardResponse> getDashboard() {
        return ResponseEntity.ok(
                dashboardService.getDashboard()
        );
    }
}