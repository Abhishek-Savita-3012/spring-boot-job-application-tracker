package com.abhishek.jobtracker.specification;

import com.abhishek.jobtracker.entity.JobApplication;
import org.springframework.data.jpa.domain.Specification;

import java.util.Locale;

public class JobApplicationSpecification {

    private JobApplicationSpecification() {
    }

    public static Specification<JobApplication> belongsToUserAndMatchesKeyword(Long userId, String keyword) {

        return (root, query, criteriaBuilder) -> {

            var belongsToUser =
                    criteriaBuilder.equal(
                            root.get("user").get("id"),
                            userId
                    );

            if (keyword == null || keyword.isBlank()) {
                return belongsToUser;
            }

            String searchPattern =
                    "%" +
                            keyword
                                    .trim()
                                    .toLowerCase(Locale.ROOT)
                            + "%";

            var companyMatches =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("company")
                            ),
                            searchPattern
                    );

            var roleMatches =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("role")
                            ),
                            searchPattern
                    );

            var locationMatches =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("location")
                            ),
                            searchPattern
                    );

            var sourceMatches =
                    criteriaBuilder.like(
                            criteriaBuilder.lower(
                                    root.get("source")
                            ),
                            searchPattern
                    );

            var keywordMatches =
                    criteriaBuilder.or(
                            companyMatches,
                            roleMatches,
                            locationMatches,
                            sourceMatches
                    );

            return criteriaBuilder.and(
                    belongsToUser,
                    keywordMatches
            );
        };
    }
}