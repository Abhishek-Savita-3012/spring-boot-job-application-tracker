package com.abhishek.jobtracker.security;

import com.abhishek.jobtracker.repository.JobApplicationRepository;
import com.jayway.jsonpath.JsonPath;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Value("${jwt.secret}")
    private String jwtSecret;

    private static final String PASSWORD = "SecurityTest123";

    private String registerAndLogin(String name, String email) throws Exception {

        String registerRequest = """
            {
              "name": "%s",
              "email": "%s",
              "password": "%s"
            }
            """.formatted(
                name,
                email,
                PASSWORD
        );

        mockMvc.perform(
                        post("/api/users/register")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(registerRequest)
                )
                .andExpect(
                        status().isCreated()
                );

        String loginRequest = """
            {
              "email": "%s",
              "password": "%s"
            }
            """.formatted(
                email,
                PASSWORD
        );

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
                                jsonPath("$.data.token")
                                        .isNotEmpty()
                        )
                        .andReturn();

        String response =
                loginResult
                        .getResponse()
                        .getContentAsString();

        return JsonPath.read(
                response,
                "$.data.token"
        );
    }

    private Long createApplication(String token, String company) throws Exception {

        String request = """
            {
              "company": "%s",
              "role": "Java Backend Developer"
            }
            """.formatted(company);

        MvcResult result =
                mockMvc.perform(
                                post("/api/applications")
                                        .header(
                                                HttpHeaders.AUTHORIZATION,
                                                "Bearer " + token
                                        )
                                        .contentType(
                                                MediaType.APPLICATION_JSON
                                        )
                                        .content(request)
                        )
                        .andExpect(
                                status().isCreated()
                        )
                        .andReturn();

        Number applicationId =
                JsonPath.read(
                        result
                                .getResponse()
                                .getContentAsString(),
                        "$.data.id"
                );

        return applicationId.longValue();
    }

    @Test
    void publicEndpoints_shouldWorkWithoutJwt()
            throws Exception {

        mockMvc.perform(
                        get("/api/health")
                )
                .andExpect(
                        status().isOk()
                );

        String registerRequest = """
            {
              "name": "Public Test User",
              "email": "public@example.com",
              "password": "SecurityTest123"
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
                );

        String loginRequest = """
            {
              "email": "public@example.com",
              "password": "SecurityTest123"
            }
            """;

        mockMvc.perform(
                        post("/api/users/login")
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(loginRequest)
                )
                .andExpect(
                        status().isOk()
                );
    }

    @Test
    void protectedEndpoint_withoutJwt_shouldReturn401()
            throws Exception {

        mockMvc.perform(
                        get("/api/applications")
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void protectedEndpoint_withInvalidJwt_shouldReturn401()
            throws Exception {

        mockMvc.perform(
                        get("/api/applications")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer this.is.not.a.valid.jwt"
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    private String createExpiredToken(
            String email
    ) {

        byte[] keyBytes =
                Decoders.BASE64.decode(
                        jwtSecret
                );

        SecretKey key =
                Keys.hmacShaKeyFor(
                        keyBytes
                );

        Instant now =
                Instant.now();

        return Jwts.builder()
                .subject(email)
                .issuedAt(
                        Date.from(
                                now.minusSeconds(3600)
                        )
                )
                .expiration(
                        Date.from(
                                now.minusSeconds(60)
                        )
                )
                .signWith(key)
                .compact();
    }

    @Test
    void protectedEndpoint_withExpiredJwt_shouldReturn401()
            throws Exception {

        String email =
                "expired@example.com";

        registerAndLogin(
                "Expired Token User",
                email
        );

        String expiredToken =
                createExpiredToken(email);

        mockMvc.perform(
                        get("/api/applications")
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + expiredToken
                                )
                )
                .andExpect(
                        status().isUnauthorized()
                );
    }

    @Test
    void userA_cannotReadUserBApplication()
            throws Exception {

        String userAToken =
                registerAndLogin(
                        "User A",
                        "read-a@example.com"
                );

        String userBToken =
                registerAndLogin(
                        "User B",
                        "read-b@example.com"
                );

        Long userBApplicationId =
                createApplication(
                        userBToken,
                        "User B Company"
                );

        mockMvc.perform(
                        get(
                                "/api/applications/{id}",
                                userBApplicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                )
                .andExpect(
                        status().isNotFound()
                )
                .andExpect(
                        jsonPath("$.success")
                                .value(false)
                )
                .andExpect(
                        jsonPath("$.status")
                                .value(404)
                );
    }

    @Test
    void userA_cannotUpdateUserBApplication()
            throws Exception {

        String userAToken =
                registerAndLogin(
                        "User A",
                        "update-a@example.com"
                );

        String userBToken =
                registerAndLogin(
                        "User B",
                        "update-b@example.com"
                );

        Long applicationId =
                createApplication(
                        userBToken,
                        "Protected Company"
                );

        String updateRequest = """
            {
              "role": "Hacked Role"
            }
            """;

        mockMvc.perform(
                        put(
                                "/api/applications/{id}",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                                .contentType(
                                        MediaType.APPLICATION_JSON
                                )
                                .content(updateRequest)
                )
                .andExpect(
                        status().isNotFound()
                );
    }

    @Test
    void userA_cannotDeleteUserBApplication()
            throws Exception {

        String userAToken =
                registerAndLogin(
                        "User A",
                        "delete-a@example.com"
                );

        String userBToken =
                registerAndLogin(
                        "User B",
                        "delete-b@example.com"
                );

        Long applicationId =
                createApplication(
                        userBToken,
                        "Cannot Delete Me"
                );

        mockMvc.perform(
                        delete(
                                "/api/applications/{id}",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                )
                .andExpect(
                        status().isNotFound()
                );

        assertTrue(
                jobApplicationRepository
                        .findById(applicationId)
                        .isPresent()
        );
    }

    @Test
    void userA_cannotAccessUserBChildResources()
            throws Exception {

        String userAToken =
                registerAndLogin(
                        "User A",
                        "child-a@example.com"
                );

        String userBToken =
                registerAndLogin(
                        "User B",
                        "child-b@example.com"
                );

        Long applicationId =
                createApplication(
                        userBToken,
                        "Private Application"
                );

        mockMvc.perform(
                        get(
                                "/api/applications/{id}/notes",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                )
                .andExpect(
                        status().isNotFound()
                );

        mockMvc.perform(
                        get(
                                "/api/applications/{id}/interviews",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                )
                .andExpect(
                        status().isNotFound()
                );

        mockMvc.perform(
                        get(
                                "/api/applications/{id}/reminders",
                                applicationId
                        )
                                .header(
                                        HttpHeaders.AUTHORIZATION,
                                        "Bearer "
                                                + userAToken
                                )
                )
                .andExpect(
                        status().isNotFound()
                );
    }


}