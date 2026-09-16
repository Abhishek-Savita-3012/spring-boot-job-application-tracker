package com.abhishek.jobtracker.mapper;

import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.Resume;
import org.springframework.stereotype.Component;

@Component
public class JobApplicationMapper {

    public JobApplicationResponse toResponse(
            JobApplication application
    ) {

        Resume resume =
                application.getResumeUsed();

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

                application.isArchived(),
                application.getArchivedAt(),

                application.getCreatedAt(),
                application.getUpdatedAt()
        );
    }
}