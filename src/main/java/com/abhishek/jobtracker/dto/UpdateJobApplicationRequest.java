package com.abhishek.jobtracker.dto;

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
public class UpdateJobApplicationRequest {

    @Size(max = 150, message = "Company cannot exceed 150 characters")
    @Pattern(regexp = ".*\\S.*", message = "Company cannot be blank")
    private String company;

    @Size(max = 150, message = "Role cannot exceed 150 characters")
    @Pattern(regexp = ".*\\S.*", message = "Role cannot be blank")
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
    @Size(max = 1000)
    private String jobUrl;

    @Size(max = 150)
    private String source;

    @PastOrPresent(message = "Applied date cannot be in the future")
    private LocalDate appliedDate;

    private LocalDate deadline;

    @Size(max = 5000, message = "Description is too long")
    private String description;

    @AssertTrue(message = "Deadline cannot be before the applied date")
    public boolean isDateRangeValid() {

        if (appliedDate == null || deadline == null) {
            return true;
        }

        return !deadline.isBefore(appliedDate);
    }
}