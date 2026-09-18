package com.abhishek.jobtracker.service;

import com.abhishek.jobtracker.dto.LoginRequest;
import com.abhishek.jobtracker.dto.RegisterRequest;
import com.abhishek.jobtracker.dto.UserResponse;
import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.exception.DuplicateEmailException;
import com.abhishek.jobtracker.exception.InvalidCredentialsException;
import com.abhishek.jobtracker.exception.ResourceNotFoundException;
import com.abhishek.jobtracker.repository.UserRepository;
import com.abhishek.jobtracker.security.JwtService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private UserService userService;

    @Test
    void registerUser_shouldSaveUserWithEncodedPassword() {

        RegisterRequest request = new RegisterRequest();

        request.setName("Abhishek");
        request.setEmail("abhishek@example.com");
        request.setPassword("Test@12345");

        when(
                userRepository.existsByEmail(
                        "abhishek@example.com"
                )
        ).thenReturn(false);

        when(
                passwordEncoder.encode(
                        "Test@12345"
                )
        ).thenReturn("encoded-password");

        when(
                userRepository.save(any(User.class))
        ).thenAnswer(invocation -> {

            User user =
                    invocation.getArgument(0);

            user.setId(1L);

            return user;
        });

        UserResponse response =
                userService.registerUser(request);

        assertNotNull(response);

        assertEquals(
                "abhishek@example.com",
                response.getEmail()
        );

        ArgumentCaptor<User> userCaptor =
                ArgumentCaptor.forClass(
                        User.class
                );

        verify(userRepository)
                .save(userCaptor.capture());

        User savedUser =
                userCaptor.getValue();

        assertEquals(
                "Abhishek",
                savedUser.getName()
        );

        assertEquals(
                "abhishek@example.com",
                savedUser.getEmail()
        );

        assertEquals(
                "encoded-password",
                savedUser.getPassword()
        );

        assertNotEquals(
                "Test@12345",
                savedUser.getPassword()
        );

        verify(passwordEncoder)
                .encode("Test@12345");
    }

    @Test
    void registerUser_shouldThrowExceptionWhenEmailAlreadyExists() {

        RegisterRequest request =
                new RegisterRequest();

        request.setName("Abhishek");
        request.setEmail("existing@example.com");
        request.setPassword("Test@12345");

        when(
                userRepository.existsByEmail(
                        "existing@example.com"
                )
        ).thenReturn(true);

        DuplicateEmailException exception =
                assertThrows(
                        DuplicateEmailException.class,
                        () ->
                                userService.registerUser(
                                        request
                                )
                );

        assertEquals(
                "An account with this email already exists",
                exception.getMessage()
        );

        verify(
                userRepository,
                never()
        ).save(any(User.class));

        verify(
                passwordEncoder,
                never()
        ).encode(anyString());
    }

    @Test
    void getUserByEmail_shouldThrowResourceNotFoundWhenUserDoesNotExist() {

        String email =
                "missing@example.com";

        when(
                userRepository.findByEmail(email)
        ).thenReturn(Optional.empty());

        ResourceNotFoundException exception =
                assertThrows(
                        ResourceNotFoundException.class,
                        () ->
                                userService.getUserByEmail(
                                        email
                                )
                );

        assertEquals(
                "User not found",
                exception.getMessage()
        );

        verify(userRepository)
                .findByEmail(email);
    }

    @Test
    void loginUser_shouldThrowInvalidCredentialsWhenPasswordIsWrong() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "abhishek@example.com"
        );

        request.setPassword(
                "WrongPassword"
        );

        User user =
                new User();

        user.setId(1L);
        user.setEmail(
                "abhishek@example.com"
        );
        user.setPassword(
                "stored-encoded-password"
        );

        when(
                userRepository.findByEmail(
                        "abhishek@example.com"
                )
        ).thenReturn(
                Optional.of(user)
        );

        when(
                passwordEncoder.matches(
                        "WrongPassword",
                        "stored-encoded-password"
                )
        ).thenReturn(false);

        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () ->
                                userService.loginUser(
                                        request
                                )
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verifyNoInteractions(jwtService);
    }

    @Test
    void loginUser_shouldThrowSameExceptionWhenEmailDoesNotExist() {

        LoginRequest request =
                new LoginRequest();

        request.setEmail(
                "unknown@example.com"
        );

        request.setPassword(
                "SomePassword123"
        );

        when(
                userRepository.findByEmail(
                        "unknown@example.com"
                )
        ).thenReturn(
                Optional.empty()
        );

        InvalidCredentialsException exception =
                assertThrows(
                        InvalidCredentialsException.class,
                        () ->
                                userService.loginUser(
                                        request
                                )
                );

        assertEquals(
                "Invalid email or password",
                exception.getMessage()
        );

        verifyNoInteractions(jwtService);
    }
}