package com.abhishek.jobtracker.controller;

import com.abhishek.jobtracker.dto.ApiResponse;
import com.abhishek.jobtracker.dto.DashboardResponse;
import com.abhishek.jobtracker.service.DashboardService;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@Tag(
        name = "Dashboard",
        description = "Authenticated user's job application analytics"
)
@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    private final DashboardService dashboardService;

    public DashboardController(DashboardService dashboardService) {
        this.dashboardService = dashboardService;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard() {

        DashboardResponse dashboard = dashboardService.getDashboard();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Dashboard analytics fetched successfully",
                        dashboard
                )
        );
    }
}