package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.InterviewRoundRequest;
import com.abhishek.jobtracker.dto.InterviewRoundResponse;
import com.abhishek.jobtracker.entity.InterviewRound;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import com.abhishek.jobtracker.repository.InterviewRoundRepository;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InterviewRoundService {

    private final InterviewRoundRepository interviewRoundRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;

    public InterviewRoundService(
            InterviewRoundRepository interviewRoundRepository,
            JobApplicationRepository jobApplicationRepository,
            CurrentUserService currentUserService
    ) {
        this.interviewRoundRepository = interviewRoundRepository;
        this.jobApplicationRepository = jobApplicationRepository;
        this.currentUserService = currentUserService;
    }

    private JobApplication getOwnedApplication(Long applicationId) {

        User user = currentUserService.getCurrentUser();

        return jobApplicationRepository.findByIdAndUser_Id(applicationId, user.getId())
                .orElseThrow(() ->
                        new ApplicationNotFoundException(
                                "Job application not found"
                        )
                );
    }

    public InterviewRoundResponse createInterviewRound(Long applicationId, InterviewRoundRequest request) {

        JobApplication application =
                getOwnedApplication(applicationId);

        InterviewRound round =
                InterviewRound.builder()
                        .roundNumber(request.getRoundNumber())
                        .title(request.getTitle())
                        .roundType(request.getRoundType())
                        .scheduledAt(request.getScheduledAt())
                        .interviewer(request.getInterviewer())
                        .outcome(request.getOutcome())
                        .notes(request.getNotes())
                        .jobApplication(application)
                        .build();

        InterviewRound savedRound = interviewRoundRepository.save(round);

        return mapToResponse(savedRound);
    }

    public List<InterviewRoundResponse> getAllInterviewRounds(Long applicationId) {

        getOwnedApplication(applicationId);

        return interviewRoundRepository
                .findAllByJobApplication_IdOrderByRoundNumberAsc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public InterviewRoundResponse getInterviewRound(Long applicationId, Long roundId) {

        getOwnedApplication(applicationId);

        InterviewRound round =
                interviewRoundRepository.findByIdAndJobApplication_Id(roundId, applicationId)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Interview round not found"
                                )
                        );

        return mapToResponse(round);
    }

    public InterviewRoundResponse updateInterviewRound(Long applicationId, Long roundId, InterviewRoundRequest request) {

        getOwnedApplication(applicationId);

        InterviewRound round = interviewRoundRepository.findByIdAndJobApplication_Id(roundId, applicationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Interview round not found"
                        )
                );

        round.setRoundNumber(request.getRoundNumber());
        round.setTitle(request.getTitle());
        round.setRoundType(request.getRoundType());
        round.setScheduledAt(request.getScheduledAt());
        round.setInterviewer(request.getInterviewer());

        if (request.getOutcome() != null) {
            round.setOutcome(request.getOutcome());
        }

        round.setNotes(request.getNotes());

        InterviewRound updatedRound = interviewRoundRepository.save(round);

        return mapToResponse(updatedRound);
    }

    public void deleteInterviewRound(Long applicationId, Long roundId) {

        getOwnedApplication(applicationId);

        InterviewRound round = interviewRoundRepository.findByIdAndJobApplication_Id(roundId, applicationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Interview round not found"
                        )
                );

        interviewRoundRepository.delete(round);
    }

    private InterviewRoundResponse mapToResponse(InterviewRound round) {

        return new InterviewRoundResponse(
                round.getId(),
                round.getRoundNumber(),
                round.getTitle(),
                round.getRoundType(),
                round.getScheduledAt(),
                round.getInterviewer(),
                round.getOutcome(),
                round.getNotes(),
                round.getCreatedAt(),
                round.getUpdatedAt()
        );
    }
}