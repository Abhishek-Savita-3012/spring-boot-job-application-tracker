package com.abhishek.jobtracker.integration;

import com.abhishek.jobtracker.entity.User;
import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.abhishek.jobtracker.repository.UserRepository;
import com.jayway.jsonpath.JsonPath;

import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class JobApplicationFlowIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Test
    void registerUser_shouldPersistUserInDatabase()
            throws Exception {

        String requestBody = """
            {
              "name": "Integration User",
              "email": "integration@example.com",
              "password": "IntegrationPass123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$.data.email")
                                .value(
                                        "integration@example.com"
                                )
                );

        User user =
                userRepository
                        .findByEmail(
                                "integration@example.com"
                        )
                        .orElseThrow();

        assertEquals(
                "Integration User",
                user.getName()
        );

        assertEquals(
                "integration@example.com",
                user.getEmail()
        );

        assertNotEquals(
                "IntegrationPass123",
                user.getPassword()
        );
    }

    @Test
    void completeJobApplicationFlow_shouldWork()
            throws Exception {

        /*
         * STEP 1
         * Register user
         */

        String registerRequest = """
            {
              "name": "Job Tracker Integration User",
              "email": "flow@example.com",
              "password": "IntegrationPass123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(registerRequest)
                )
                .andExpect(
                        status().isCreated()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                );


        /*
         * STEP 2
         * Login
         */

        String loginRequest = """
            {
              "email": "flow@example.com",
              "password": "IntegrationPass123"
            }
            """;

        MvcResult loginResult =
                mockMvc.perform(
                                post("/api/users/login")
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(loginRequest)
                        )
                        .andExpect(
                                status().isOk()
                        )
                        .andExpect(
                                jsonPath("$.success")
                                        .value(true)
                        )
                        .andExpect(
                                jsonPath("$.data.token")
                                        .isNotEmpty()
                        )
                        .andReturn();


        /*
         * STEP 3
         * Extract JWT
         */

        String loginJson =
                loginResult
                        .getResponse()
                        .getContentAsString();

        String token =
                JsonPath.read(
                        loginJson,
                        "$.data.token"
                );

        assertNotNull(token);
        assertFalse(token.isBlank());


        /*
         * STEP 4
         * Create job application
         */

        String applicationRequest = """
            {
              "company": "Integration Corp",
              "role": "Java Backend Developer",
              "location": "Remote",
              "employmentType": "FULL_TIME",
              "workMode": "REMOTE",
              "salary": 800000,
              "salaryCurrency": "INR",
              "jobUrl": "https://example.com/integration-job",
              "source": "Integration Test",
              "appliedDate": "2026-09-18",
              "deadline": "2026-09-30",
              "description": "Application created from integration test."
            }
            """;

        MvcResult createResult =
                mockMvc.perform(
                                post("/api/applications")
                                        .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + token
                                        )
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(
                                                applicationRequest
                                        )
                        )
                        .andExpect(
                                status().isCreated()
                        )
                        .andExpect(
                                jsonPath("$.success")
                                        .value(true)
                        )
                        .andExpect(
                                jsonPath("$.data.company")
                                        .value(
                                                "Integration Corp"
                                        )
                        )
                        .andExpect(
                                jsonPath("$.data.role")
                                        .value(
                                                "Java Backend Developer"
                                        )
                        )
                        .andReturn();


        /*
         * STEP 5
         * Extract application ID
         */

        Number applicationIdValue =
                JsonPath.read(
                        createResult
                                .getResponse()
                                .getContentAsString(),
                        "$.data.id"
                );

        Long applicationId =
                applicationIdValue.longValue();

        assertNotNull(applicationId);


        /*
         * STEP 6
         * Verify actual DB record
         */

        User user =
                userRepository
                        .findByEmail(
                                "flow@example.com"
                        )
                        .orElseThrow();

        assertTrue(
                jobApplicationRepository
                        .findByIdAndUser_Id(
                                applicationId,
                                user.getId()
                        )
                        .isPresent()
        );


        /*
         * STEP 7
         * Retrieve through API
         */

        mockMvc.perform(
                        get(
                                "/api/applications/{id}",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer " + token
                                )
                )
                .andExpect(
                        status().isOk()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(true)
                )
                .andExpect(
                        jsonPath("$.data.id")
                                .value(applicationId)
                )
                .andExpect(
                        jsonPath("$.data.company")
                                .value(
                                        "Integration Corp"
                                )
                )
                .andExpect(
                        jsonPath("$.data.role")
                                .value(
                                        "Java Backend Developer"
                                )
                );
    }

    @Test
    void registerUser_withInvalidRequest_shouldReturn400()
            throws Exception {

        String requestBody = """
            {
              "name": "",
              "email": "not-an-email",
              "password": "123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isBadRequest()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(false)
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(400)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "Validation failed"
                                )
                )
                .andExpect(
                        jsonPath("$.errors.email")
                                .exists()
                )
                .andExpect(
                        jsonPath("$.errors.password")
                                .exists()
                );

        assertTrue(
                userRepository
                        .findByEmail(
                                "not-an-email"
                        )
                        .isEmpty()
        );
    }

    @Test
    void registerUser_withDuplicateEmail_shouldReturn409()
            throws Exception {

        String requestBody = """
            {
              "name": "First User",
              "email": "duplicate@example.com",
              "password": "IntegrationPass123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(requestBody)
                )
                .andExpect(
                        status().isCreated()
                );

        String duplicateRequest = """
            {
              "name": "Second User",
              "email": "duplicate@example.com",
              "password": "AnotherPass123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(
                                        duplicateRequest
                                )
                )
                .andExpect(
                        status().isConflict()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(false)
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(409)
                )
                .andExpect(
                        jsonPath("$.message")
                                .value(
                                        "An account with this email already exists"
                                )
                );
    }


}