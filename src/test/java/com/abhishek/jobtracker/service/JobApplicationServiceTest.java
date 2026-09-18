package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.JobApplicationResponse;
import com.abhishek.jobtracker.entity.JobApplication;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.ApplicationNotFoundException;
import com.abhishek.jobtracker.mapper.JobApplicationMapper;
import com.abhishek.jobtracker.repository.*;
import com.abhishek.jobtracker.security.CurrentUserService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class JobApplicationServiceTest {

    @Mock
    private JobApplicationRepository jobApplicationRepository;

    @Mock
    private CurrentUserService currentUserService;

    @Mock
    private StatusHistoryRepository statusHistoryRepository;

    @Mock
    private InterviewRoundRepository interviewRoundRepository;

    @Mock
    private ApplicationNoteRepository applicationNoteRepository;

    @Mock
    private FollowUpReminderRepository reminderRepository;

    @Mock
    private ResumeRepository resumeRepository;

    @Mock
    private JobApplicationMapper jobApplicationMapper;

    @InjectMocks
    private JobApplicationService jobApplicationService;

    @Test
    void getApplicationById_shouldReturnApplicationOwnedByCurrentUser() {

        Long userId = 1L;
        Long applicationId = 10L;

        User user =
                new User();

        user.setId(userId);

        JobApplication application =
                new JobApplication();

        application.setId(
                applicationId
        );

        JobApplicationResponse expectedResponse =
                mock(
                        JobApplicationResponse.class
                );

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                jobApplicationRepository
                        .findByIdAndUser_Id(
                                applicationId,
                                userId
                        )
        ).thenReturn(
                Optional.of(application)
        );

        when(
                jobApplicationMapper
                        .toResponse(application)
        ).thenReturn(
                expectedResponse
        );

        JobApplicationResponse actualResponse =
                jobApplicationService
                        .getApplicationById(
                                applicationId
                        );

        assertSame(
                expectedResponse,
                actualResponse
        );

        verify(jobApplicationRepository)
                .findByIdAndUser_Id(
                        applicationId,
                        userId
                );

        verify(jobApplicationMapper)
                .toResponse(application);
    }

    @Test
    void getApplicationById_shouldThrowNotFoundWhenApplicationIsNotOwnedByUser() {

        Long userId = 1L;
        Long applicationId = 50L;

        User user =
                new User();

        user.setId(userId);

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                jobApplicationRepository
                        .findByIdAndUser_Id(
                                applicationId,
                                userId
                        )
        ).thenReturn(
                Optional.empty()
        );

        assertThrows(
                ApplicationNotFoundException.class,
                () ->
                        jobApplicationService
                                .getApplicationById(
                                        applicationId
                                )
        );

        verify(
                jobApplicationMapper,
                never()
        ).toResponse(any());
    }

    @Test
    void archiveApplication_shouldMarkApplicationAsArchived() {

        Long userId = 1L;
        Long applicationId = 10L;

        User user =
                new User();

        user.setId(userId);

        JobApplication application =
                new JobApplication();

        application.setId(
                applicationId
        );

        application.setArchived(false);

        JobApplicationResponse response =
                mock(
                        JobApplicationResponse.class
                );

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                jobApplicationRepository
                        .findByIdAndUser_Id(
                                applicationId,
                                userId
                        )
        ).thenReturn(
                Optional.of(application)
        );

        when(
                jobApplicationRepository.save(
                        application
                )
        ).thenReturn(
                application
        );

        when(
                jobApplicationMapper
                        .toResponse(application)
        ).thenReturn(response);

        jobApplicationService
                .archiveApplication(
                        applicationId
                );

        assertTrue(
                application.isArchived()
        );

        assertNotNull(
                application.getArchivedAt()
        );

        verify(jobApplicationRepository)
                .save(application);
    }

    @Test
    void restoreApplication_shouldMakeApplicationActiveAgain() {

        Long userId = 1L;
        Long applicationId = 10L;

        User user =
                new User();

        user.setId(userId);

        JobApplication application =
                new JobApplication();

        application.setId(
                applicationId
        );

        application.setArchived(true);

        application.setArchivedAt(
                LocalDateTime.now()
                        .minusDays(2)
        );

        JobApplicationResponse response =
                mock(
                        JobApplicationResponse.class
                );

        when(
                currentUserService.getCurrentUser()
        ).thenReturn(user);

        when(
                jobApplicationRepository
                        .findByIdAndUser_Id(
                                applicationId,
                                userId
                        )
        ).thenReturn(
                Optional.of(application)
        );

        when(
                jobApplicationRepository.save(
                        application
                )
        ).thenReturn(application);

        when(
                jobApplicationMapper
                        .toResponse(application)
        ).thenReturn(response);

        jobApplicationService
                .restoreApplication(
                        applicationId
                );

        assertFalse(
                application.isArchived()
        );

        assertNull(
                application.getArchivedAt()
        );

        verify(jobApplicationRepository)
                .save(application);
    }
}