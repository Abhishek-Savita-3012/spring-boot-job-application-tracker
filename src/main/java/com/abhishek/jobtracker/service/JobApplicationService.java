package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.CreateJobApplicationRequest;
import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.dto.UpdateJobApplicationRequest;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.repository.UserRepository;
import org.springframework.stereotype.Service;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import java.util.List;

@Service
public class JobApplicationService {

    private final JobApplicationRepository jobApplicationRepository;
    private final UserRepository userRepository;

    public JobApplicationService(JobApplicationRepository jobApplicationRepository, UserRepository userRepository) {
        this.jobApplicationRepository = jobApplicationRepository;
        this.userRepository = userRepository;
    }

    public JobApplicationResponse createApplication(CreateJobApplicationRequest request, String userEmail) {

        User user = userRepository.findByEmail(userEmail)
                .orElseThrow(() ->
                        new RuntimeException("User not found")
                );

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

    public List<JobApplicationResponse> getAllApplications(String userEmail) {

        return jobApplicationRepository
                .findAllByUser_Email(userEmail)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public JobApplicationResponse getApplicationById(Long id, String userEmail) {

        JobApplication application =
                jobApplicationRepository
                        .findByIdAndUser_Email(id, userEmail)
                        .orElseThrow(() ->
                                new ApplicationNotFoundException(
                                        "Job application not found"
                                )
                        );

        return mapToResponse(application);
    }

    public JobApplicationResponse updateApplication(Long id, UpdateJobApplicationRequest request, String userEmail) {

        JobApplication application =
                jobApplicationRepository
                        .findByIdAndUser_Email(id, userEmail)
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

    public void deleteApplication(Long id, String userEmail) {

        JobApplication application =
                jobApplicationRepository
                        .findByIdAndUser_Email(id, userEmail)
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