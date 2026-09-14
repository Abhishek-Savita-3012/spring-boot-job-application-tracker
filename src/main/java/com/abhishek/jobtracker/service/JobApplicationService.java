package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.CreateJobApplicationRequest;
import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.dto.UpdateJobApplicationRequest;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, CurrentUserService currentUserService) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.currentUserService = currentUserService;
    }

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

        if (request.getStatus() != null) {
            application.setStatus(request.getStatus());
        }

        application.setAppliedDate(request.getAppliedDate());
        application.setDeadline(request.getDeadline());
        application.setDescription(request.getDescription());

        JobApplication updatedApplication = jobApplicationRepository.save(application);

        return mapToResponse(updatedApplication);
    }

    public void deleteApplication(Long id) {

        User user = currentUserService.getCurrentUser();

        JobApplication application = jobApplicationRepository.findByIdAndUser_Id(id, user.getId())
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        jobApplicationRepository.delete(application);
    }

    private JobApplicationResponse mapToResponse(JobApplication application) {

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
                application.getDescription(),
                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}