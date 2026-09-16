package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Map;

@Getter
@AllArgsConstructor
public class DashboardResponse {

    private long totalActiveApplications;

    private long totalArchivedApplications;

    private long applicationsThisMonth;

    private long upcomingInterviews;

    private long pendingReminders;

    private long offers;

    private long rejections;

    private Map<ApplicationStatus, Long> applicationsByStatus;

    private Map<WorkMode, Long> applicationsByWorkMode;

    private Map<EmploymentType, Long> applicationsByEmploymentType;
}