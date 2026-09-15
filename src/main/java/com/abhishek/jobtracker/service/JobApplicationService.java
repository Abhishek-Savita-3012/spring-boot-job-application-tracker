package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.CreateJobApplicationRequest;
import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.dto.UpdateJobApplicationRequest;
import com.abhishek.jobtracker.entity.*;
import com.abhishek.jobtracker.repository.*;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import java.util.List;
import org.springframework.transaction.annotation.Transactional;
import com.abhishek.jobtracker.dto.StatusHistoryResponse;
import com.abhishek.jobtracker.repository.ResumeRepository;
import com.abhishek.jobtracker.specification.JobApplicationSpecification;
import com.abhishek.jobtracker.exception.InvalidSortException;
import org.springframework.data.domain.Sort;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;
    private final StatusHistoryRepository statusHistoryRepository;
    private final InterviewRoundRepository interviewRoundRepository;
    private final ApplicationNoteRepository applicationNoteRepository;
    private final ResumeRepository resumeRepository;
    private final FollowUpReminderRepository followUpReminderRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, CurrentUserService currentUserService, StatusHistoryRepository statusHistoryRepository, InterviewRoundRepository interviewRoundRepository, ApplicationNoteRepository applicationNoteRepository, ResumeRepository resumeRepository, FollowUpReminderRepository followUpReminderRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.currentUserService = currentUserService;
        this.statusHistoryRepository = statusHistoryRepository;
        this.interviewRoundRepository = interviewRoundRepository;
        this.applicationNoteRepository = applicationNoteRepository;
        this.resumeRepository = resumeRepository;
        this.followUpReminderRepository = followUpReminderRepository;
    }

    @Transactional
    public JobApplicationResponse createApplication(CreateJobApplicationRequest request) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = JobApplication.builder()
                .company(request.getCompany())
                .role(request.getRole())
                .location(request.getLocation())
                .employmentType(request.getEmploymentType())
                .workMode(request.getWorkMode())
                .salary(request.getSalary())
                .salaryCurrency(request.getSalaryCurrency())
                .jobUrl(request.getJobUrl())
                .source(request.getSource())
                .status(request.getStatus())
                .appliedDate(request.getAppliedDate())
                .deadline(request.getDeadline())
                .description(request.getDescription())
                .user(user)
                .build();

        JobApplication savedApplication = jobApplicationRepository.save(application);

        StatusHistory initialHistory =
                StatusHistory.builder()
                        .oldStatus(null)
                        .newStatus(savedApplication.getStatus())
                        .jobApplication(savedApplication)
                        .build();

        statusHistoryRepository.save(initialHistory);

        return mapToResponse(savedApplication);
    }

    public List<JobApplicationResponse> getAllApplications() {

        User user = currentUserService.getCurrentUser();

        return jobApplicationRepository
                .findAllByUser_Id(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse getApplicationById(Long id) {

        User user = currentUserService.getCurrentUser();

        JobApplication application =
                jobApplicationRepository.findByIdAndUser_Id(id, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        return mapToResponse(application);
    }

    public JobApplicationResponse updateApplication(Long id, UpdateJobApplicationRequest request) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(id, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        application.setCompany(request.getCompany());
        application.setRole(request.getRole());
        application.setLocation(request.getLocation());
        application.setEmploymentType(request.getEmploymentType());
        application.setWorkMode(request.getWorkMode());
        application.setSalary(request.getSalary());
        application.setSalaryCurrency(request.getSalaryCurrency());
        application.setJobUrl(request.getJobUrl());
        application.setSource(request.getSource());

        application.setAppliedDate(request.getAppliedDate());
        application.setDeadline(request.getDeadline());
        application.setDescription(request.getDescription());

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    @Transactional
    public void deleteApplication(Long id) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(id, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        followUpReminderRepository.deleteAllByJobApplication_Id(id);
        applicationNoteRepository.deleteAllByJobApplication_Id(id);
        interviewRoundRepository.deleteAllByJobApplication_Id(id);
        statusHistoryRepository.deleteAllByJobApplication_Id(id);
        jobApplicationRepository.delete(application);
    }

    @Transactional
    public JobApplicationResponse updateStatus(Long id, ApplicationStatus newStatus) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(id, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        ApplicationStatus oldStatus = application.getStatus();

        if (oldStatus == newStatus) {
            return mapToResponse(application);
        }

        application.setStatus(newStatus);

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        StatusHistory history =
                StatusHistory.builder()
                        .oldStatus(oldStatus)
                        .newStatus(newStatus)
                        .jobApplication(updatedApplication)
                        .build();

        statusHistoryRepository.save(history);

        return mapToResponse(updatedApplication);
    }

    public List<StatusHistoryResponse> getStatusHistory(Long applicationId) {

        User user = currentUserService.getCurrentUser();

        jobApplicationRepository.findByIdAndUser_Id(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Job application not found"
                        )
                );

        return statusHistoryRepository
                .findAllByJobApplication_IdOrderByChangedAtAsc(applicationId)
                .stream()
                .map(this::mapHistoryToResponse)
                .toList();
    }

    private StatusHistoryResponse mapHistoryToResponse(StatusHistory history) {

        return new StatusHistoryResponse(
                history.getId(),
                history.getOldStatus(),
                history.getNewStatus(),
                history.getChangedAt()
        );
    }

    public JobApplicationResponse attachResume(Long applicationId, Long resumeId) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(applicationId, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        Resume resume = resumeRepository.findByIdAndUser_Id(resumeId, user.getId())
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Resume not found"
                                )
                        );

        application.setResumeUsed(resume);

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    public JobApplicationResponse detachResume(Long applicationId) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(applicationId, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        application.setResumeUsed(null);

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    private JobApplicationResponse mapToResponse(JobApplication application) {

        Resume resume = application.getResumeUsed();

        return new JobApplicationResponse(
                application.getId(),
                application.getCompany(),
                application.getRole(),
                application.getLocation(),
                application.getEmploymentType(),
                application.getWorkMode(),
                application.getSalary(),
                application.getSalaryCurrency(),
                application.getJobUrl(),
                application.getSource(),
                application.getStatus(),
                application.getAppliedDate(),
                application.getDeadline(),

                resume != null ? resume.getId() : null,
                resume != null ? resume.getLabel() : null,

                application.getDescription(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }

    public List<JobApplicationResponse> searchApplications(String keyword) {

        User user = currentUserService.getCurrentUser();

        var specification = JobApplicationSpecification.withFilters(user.getId(), keyword, null, null, null);

        return jobApplicationRepository
                .findAll(specification)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public List<JobApplicationResponse> filterApplications(
            String keyword,
            ApplicationStatus status,
            WorkMode workMode,
            EmploymentType employmentType,
            String sortBy,
            String direction
    ) {

        User user = currentUserService.getCurrentUser();

        var specification =
                JobApplicationSpecification.withFilters(
                        user.getId(),
                        keyword,
                        status,
                        workMode,
                        employmentType
                );

        Sort sort = buildSort(sortBy, direction);

        return jobApplicationRepository
                .findAll(specification, sort)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private Sort buildSort(String sortBy, String direction) {

        String sortField = switch (sortBy) {

            case "createdAt" -> "createdAt";
            case "updatedAt" -> "updatedAt";
            case "company" -> "company";
            case "role" -> "role";
            case "salary" -> "salary";
            case "appliedDate" -> "appliedDate";
            case "deadline" -> "deadline";
            case "status" -> "status";

            default -> throw new InvalidSortException(
                    "Invalid sort field: " + sortBy
            );
        };

        Sort.Direction sortDirection;

        if ("asc".equalsIgnoreCase(direction)) {

            sortDirection = Sort.Direction.ASC;

        } else if ("desc".equalsIgnoreCase(direction)) {

            sortDirection = Sort.Direction.DESC;

        } else {

            throw new InvalidSortException(
                    "Sort direction must be 'asc' or 'desc'"
            );
        }

        return Sort.by(sortDirection, sortField);
    }
}