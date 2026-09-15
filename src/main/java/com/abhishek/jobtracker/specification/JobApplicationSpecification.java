package com.abhishek.jobtracker.specification;

import com.abhishek.jobtracker.entity.ApplicationStatus;
import com.abhishek.jobtracker.entity.EmploymentType;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.WorkMode;
import jakarta.persistence.criteria.Predicate;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class JobApplicationSpecification {

    private JobApplicationSpecification() {
    }

    public static Specification<JobApplication> withFilters(
            Long userId,
            String keyword,
            ApplicationStatus status,
            WorkMode workMode,
            EmploymentType employmentType
    ) {

        return (root, query, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            /*
             * Security condition:
             * Always restrict results to current user.
             */
            predicates.add(
                    criteriaBuilder.equal(
                            root.get("user").get("id"),
                            userId
                    )
            );

            /*
             * Keyword search
             */
            if (keyword != null
                    && !keyword.isBlank()) {

                String searchPattern =
                        "%"
                                + keyword
                                .trim()
                                .toLowerCase(Locale.ROOT)
                                + "%";

                Predicate companyMatches =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("company")
                                ),
                                searchPattern
                        );

                Predicate roleMatches =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("role")
                                ),
                                searchPattern
                        );

                Predicate locationMatches =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("location")
                                ),
                                searchPattern
                        );

                Predicate sourceMatches =
                        criteriaBuilder.like(
                                criteriaBuilder.lower(
                                        root.get("source")
                                ),
                                searchPattern
                        );

                predicates.add(
                        criteriaBuilder.or(
                                companyMatches,
                                roleMatches,
                                locationMatches,
                                sourceMatches
                        )
                );
            }

            /*
             * Status filter
             */
            if (status != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("status"),
                                status
                        )
                );
            }

            /*
             * Work mode filter
             */
            if (workMode != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("workMode"),
                                workMode
                        )
                );
            }

            /*
             * Employment type filter
             */
            if (employmentType != null) {

                predicates.add(
                        criteriaBuilder.equal(
                                root.get("employmentType"),
                                employmentType
                        )
                );
            }

            return criteriaBuilder.and(
                    predicates.toArray(
                            new Predicate[0]
                    )
            );
        };
    }
}