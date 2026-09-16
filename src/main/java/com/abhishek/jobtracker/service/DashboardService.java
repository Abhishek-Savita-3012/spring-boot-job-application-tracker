package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.DashboardResponse;
import com.abhishek.jobtracker.entity.*;
import com.abhishek.jobtracker.repository.FollowUpReminderRepository;
import com.abhishek.jobtracker.repository.InterviewRoundRepository;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class DashboardService {

    private final JobApplicationRepository jobApplicationRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final FollowUpReminderRepository reminderRepository;
    private final CurrentUserService currentUserService;

    public DashboardService(
            JobApplicationRepository jobApplicationRepository,
            InterviewRoundRepository interviewRoundRepository,
            FollowUpReminderRepository reminderRepository,
            CurrentUserService currentUserService
    ) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.interviewRoundRepository = interviewRoundRepository;
        this.reminderRepository = reminderRepository;
        this.currentUserService = currentUserService;
    }

    public DashboardResponse getDashboard() {

        User user = currentUserService.getCurrentUser();

        Long userId = user.getId();

        long totalActiveApplications = jobApplicationRepository.countByUser_IdAndArchivedFalse(userId);

        long totalArchivedApplications = jobApplicationRepository.countByUser_IdAndArchivedTrue(userId);

        LocalDate today = LocalDate.now();

        LocalDate startOfMonth = today.withDayOfMonth(1);

        LocalDate startOfNextMonth = startOfMonth.plusMonths(1);

        long applicationsThisMonth =
                jobApplicationRepository
                        .countByUser_IdAndArchivedFalseAndAppliedDateGreaterThanEqualAndAppliedDateLessThan(
                                userId,
                                startOfMonth,
                                startOfNextMonth
                        );

        long upcomingInterviews =
                interviewRoundRepository
                        .countByJobApplication_User_IdAndJobApplication_ArchivedFalseAndScheduledAtGreaterThanEqualAndOutcome(
                                userId,
                                LocalDateTime.now(),
                                InterviewOutcome.PENDING
                        );

        long pendingReminders =
                reminderRepository
                        .countByJobApplication_User_IdAndJobApplication_ArchivedFalseAndStatus(
                                userId,
                                ReminderStatus.PENDING
                        );

        long offers =
                jobApplicationRepository
                        .countByUser_IdAndArchivedFalseAndStatus(
                                userId,
                                ApplicationStatus.OFFER
                        );

        long rejections =
                jobApplicationRepository
                        .countByUser_IdAndArchivedFalseAndStatus(
                                userId,
                                ApplicationStatus.REJECTED
                        );

        Map<ApplicationStatus, Long> statusCounts = buildStatusCounts(userId);

        Map<WorkMode, Long> workModeCounts = buildWorkModeCounts(userId);

        Map<EmploymentType, Long> employmentTypeCounts = buildEmploymentTypeCounts(userId);

        return new DashboardResponse(
                totalActiveApplications,
                totalArchivedApplications,
                applicationsThisMonth,
                upcomingInterviews,
                pendingReminders,
                offers,
                rejections,
                statusCounts,
                workModeCounts,
                employmentTypeCounts
        );
    }

    private Map<ApplicationStatus, Long> buildStatusCounts(Long userId) {

        Map<ApplicationStatus, Long> counts = new EnumMap<>(ApplicationStatus.class);

        for (ApplicationStatus status : ApplicationStatus.values()) {
            counts.put(status, 0L);
        }

        List<Object[]> results = jobApplicationRepository.countApplicationsByStatus(userId);

        for (Object[] row : results) {
            ApplicationStatus status = (ApplicationStatus) row[0];
            Long count = (Long) row[1];
            counts.put(status, count);
        }
        return counts;
    }

    private Map<WorkMode, Long> buildWorkModeCounts(Long userId) {

        Map<WorkMode, Long> counts = new EnumMap<>(WorkMode.class);

        for (WorkMode workMode : WorkMode.values()) {
            counts.put(workMode, 0L);
        }

        List<Object[]> results = jobApplicationRepository.countApplicationsByWorkMode(userId);

        for (Object[] row : results) {
            WorkMode workMode = (WorkMode) row[0];
            Long count = (Long) row[1];
            counts.put(workMode, count);
        }
        return counts;
    }

    private Map<EmploymentType, Long> buildEmploymentTypeCounts(Long userId) {

        Map<EmploymentType, Long> counts = new EnumMap<>(EmploymentType.class);

        for (EmploymentType type : EmploymentType.values()) {
            counts.put(type, 0L);
        }

        List<Object[]> results = jobApplicationRepository.countApplicationsByEmploymentType(userId);

        for (Object[] row : results) {
            EmploymentType type = (EmploymentType) row[0];
            Long count = (Long) row[1];
            counts.put(type, count);
        }
        return counts;
    }
}