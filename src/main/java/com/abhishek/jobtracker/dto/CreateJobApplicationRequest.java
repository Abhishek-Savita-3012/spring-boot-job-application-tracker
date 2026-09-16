package com.abhishek.jobtracker.dto;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.WorkMode;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.URL;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
public class CreateJobApplicationRequest {

    @NotBlank(message = "Company is required")
    @Size(max = 150, message = "Company cannot exceed 150 characters")
    private String company;

    @NotBlank(message = "Role is required")
    @Size(max = 150, message = "Role cannot exceed 150 characters")
    private String role;

    @Size(max = 150, message = "Location cannot exceed 150 characters")
    private String location;

    private EmploymentType employmentType;

    private WorkMode workMode;

    @PositiveOrZero(message = "Salary cannot be negative")
    private BigDecimal salary;

    @Pattern(regexp = "^[A-Z]{3}$", message = "Salary currency must be a 3-letter uppercase code such as INR or USD")
    private String salaryCurrency;

    @URL(message = "Job URL must be a valid URL")
    @Size(max = 1000, message = "Job URL cannot exceed 1000 characters")
    private String jobUrl;

    @Size(max = 150, message = "Source cannot exceed 150 characters")
    private String source;

    private ApplicationStatus status;

    @PastOrPresent(message = "Applied date cannot be in the future")
    private LocalDate appliedDate;

    private LocalDate deadline;

    @Size(max = 5000, message = "Description cannot exceed 5000 characters")
    private String description;

    @AssertTrue(message = "Deadline cannot be before the applied date")
    public boolean isDateRangeValid() {

        if (appliedDate == null || deadline == null) {
            return true;
        }
        return !deadline.isBefore(appliedDate);
    }

    @AssertTrue(message = "Salary currency is required when salary is provided")
    public boolean isSalaryCurrencyValid() {

        if (salary == null) {
            return true;
        }

        return salaryCurrency != null
                && !salaryCurrency.isBlank();
    }
}