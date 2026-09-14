package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.ApplicationNoteRequest;
import com.abhishek.jobtracker.dto.ApplicationNoteResponse;
import com.abhishek.jobtracker.entity.ApplicationNote;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import com.abhishek.jobtracker.repository.ApplicationNoteRepository;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ApplicationNoteService {

    private final ApplicationNoteRepository applicationNoteRepository;
    private final JobApplicationRepository jobApplicationRepository;
    private final CurrentUserService currentUserService;

    public ApplicationNoteService(
            ApplicationNoteRepository applicationNoteRepository,
            JobApplicationRepository jobApplicationRepository,
            CurrentUserService currentUserService
    ) {
        this.applicationNoteRepository = applicationNoteRepository;
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

    public ApplicationNoteResponse createNote(Long applicationId, ApplicationNoteRequest request) {

        JobApplication application = getOwnedApplication(applicationId);

        ApplicationNote note =
                ApplicationNote.builder()
                        .content(request.getContent())
                        .jobApplication(application)
                        .build();

        ApplicationNote savedNote = applicationNoteRepository.save(note);

        return mapToResponse(savedNote);
    }

    public List<ApplicationNoteResponse> getAllNotes(Long applicationId) {

        getOwnedApplication(applicationId);

        return applicationNoteRepository
                .findAllByJobApplication_IdOrderByCreatedAtDesc(applicationId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public ApplicationNoteResponse getNote(Long applicationId, Long noteId) {

        getOwnedApplication(applicationId);

        ApplicationNote note = applicationNoteRepository.findByIdAndJobApplication_Id(noteId, applicationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Note not found"
                        )
                );

        return mapToResponse(note);
    }

    public ApplicationNoteResponse updateNote(Long applicationId, Long noteId, ApplicationNoteRequest request) {

        getOwnedApplication(applicationId);

        ApplicationNote note = applicationNoteRepository.findByIdAndJobApplication_Id(noteId, applicationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Note not found"
                        )
                );

        note.setContent(request.getContent());

        ApplicationNote updatedNote = applicationNoteRepository.save(note);

        return mapToResponse(updatedNote);
    }

    public void deleteNote(Long applicationId, Long noteId) {

        getOwnedApplication(applicationId);

        ApplicationNote note = applicationNoteRepository.findByIdAndJobApplication_Id(noteId, applicationId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Note not found"
                        )
                );

        applicationNoteRepository.delete(note);
    }

    private ApplicationNoteResponse mapToResponse(ApplicationNote note) {

        return new ApplicationNoteResponse(
                note.getId(),
                note.getContent(),
                note.getCreatedAt(),
                note.getUpdatedAt()
        );
    }
}