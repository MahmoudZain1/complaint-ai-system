package com.ecommerce.complaints.integration;


import com.ecommerce.complaints.common.RestClientTestUtility;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.error.UserErrors;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.ErrorVTO;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.repository.api.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class AuthIntegrationTest {

    @LocalServerPort
    private int port;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

   private RestClient authClient;


    @BeforeEach
    void setUp() {
        String baseUrl = "http://localhost:" + port + "/auth";
        authClient = RestClient.create(baseUrl);
    }


    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldSucceed_withValidData(){

        RegisterRequest request = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email("zain1@gmail.com")
                .password("password1@_ha")
                .build();

        ResponseEntity<Void> response = authClient.post()
                .uri("/register")
                .body(request)
                .retrieve()
                .toBodilessEntity();

          assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
          Optional<User> user = userRepository.findByEmail("zain1@gmail.com");
          assertThat(user).isPresent();
          User savedUser = user.get();
          assertThat(savedUser.getName()).isEqualTo("Mahmoud Zain" );
          assertThat(savedUser.getRole()).isEqualTo(UserRole.CUSTOMER);
          assertThat(passwordEncoder.matches( request.getPassword(), savedUser.getPassword())).isTrue();

    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenEmailAlreadyExists(){

        String existingEmail = "zain2@gmail.com";
        User user = User.builder().email(existingEmail).password("password1@_ha").name("Mahmoud Zain").build();
        userRepository.save(user);

        RegisterRequest requestWithDuplicateEmail = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email(existingEmail)
                .password("password1@_ha")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithDuplicateEmail)
                    .retrieve()
                    .toBodilessEntity();
                })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex -> {
                         RestClientResponseException e = (RestClientResponseException) ex;
                         assertThat(e.getStatusCode()).isEqualTo(HttpStatus.CONFLICT);
                         ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);

            assertThat(errorVTO.getCode()).isEqualTo(UserErrors.USER_ALREADY_EXISTS.getCode());
                });
            assertThat(userRepository.count()).isEqualTo(1);
    }


    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenPasswordIsTooShort(){

        RegisterRequest requestWithShortPassword = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email("zain3@gmail.com")
                .password("pass")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithShortPassword)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.PASSWORD_TOO_SHORT.getCode());

                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenEmailIsInvalid(){
        RegisterRequest requestWithInvalidEmail = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email("zain4")
                .password("password1@_ha")
                .build();
        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithInvalidEmail)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.INVALID_EMAIL.getCode());

                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenNameIsNull(){

        RegisterRequest requestWithNameIsNull = RegisterRequest.builder()
                .name("")
                .email("zain5@gmail.com")
                .password("password1@_ha")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithNameIsNull)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.MISSING_REQUIRED_FIELD.getCode());

                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenEmailIsNull(){

        RegisterRequest requestWithEmailIsNull = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email("")
                .password("password1@_ha")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithEmailIsNull)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.MISSING_REQUIRED_FIELD.getCode());

                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void register_shouldFail_whenPasswordIsNull(){

        RegisterRequest requestWithPasswordIsNull = RegisterRequest.builder()
                .name("Mahmoud Zain")
                .email("zain6@gmail.com")
                .password("")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/register")
                    .body(requestWithPasswordIsNull)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.MISSING_REQUIRED_FIELD.getCode());

                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_shouldSucceed_withValidCredentials(){

        String rawPassword = "password1@_ha";
        String userEmail = "zain1@gmail.com";

        User user = User.builder()
                .name("Mahmoud Zain")
                .email(userEmail)
                .password(passwordEncoder.encode(rawPassword))
                .active(true)
                .role(UserRole.CUSTOMER)
                .build();
        userRepository.save(user);

        LoginRequest request = LoginRequest.builder()
                .email(userEmail)
                .password(rawPassword)
                .build();

       ResponseEntity<AuthResponse> responseEntity =  authClient.post()
                .uri("/login")
                .body(request)
                .retrieve()
                .toEntity(AuthResponse.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        AuthResponse authResponse = responseEntity.getBody();
        assertThat(authResponse).isNotNull();
        assertThat(authResponse.getAccessToken()).isNotBlank();
        assertThat(authResponse.getEmail()).isEqualTo(userEmail);
        assertThat(authResponse.getName()).isEqualTo("Mahmoud Zain");
        assertThat(authResponse.getRole()).isEqualTo(AuthResponse.RoleEnum.CUSTOMER);
        assertThat(authResponse.getTokenType()).isEqualTo("Bearer");
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_shouldFail_withInvalidPassword(){

        String correctPassword = "password1@_ha";
        String userEmail = "zain2@gmail.com";
        User user = User.builder()
                .name("Mahmoud Zain")
                .email(userEmail)
                .password(passwordEncoder.encode(correctPassword))
                .active(true)
                .role(UserRole.CUSTOMER)
                .build();
        userRepository.save(user);

      LoginRequest requestWithInvalidPassword = LoginRequest.builder()
              .email(userEmail)
              .password("drowssap")
              .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/login")
                    .body(requestWithInvalidPassword)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.INVALID_CREDENTIALS.getCode());
                });
    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_shouldFail_whenUserNotFound(){

        LoginRequest requestWithNonExistentUser = LoginRequest.builder()
                .email("zain3@gmail.com")
                .password("drowssap")
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/login")
                    .body(requestWithNonExistentUser)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.INVALID_CREDENTIALS.getCode());
                });

    }

    @Test
    @Sql(scripts = "classpath:sql/clear-user.sql" , executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    void login_shouldFail_whenUserIsInactive(){

        String rawPassword = "password1@_ha";
        String userEmail = "zain1@gmail.com";

        User user = User.builder()
                .name("Mahmoud Zain")
                .email(userEmail)
                .password(passwordEncoder.encode(rawPassword))
                .role(UserRole.CUSTOMER)
                .build();
        userRepository.save(user);

        LoginRequest requestWithUserIsInactive = LoginRequest.builder()
                .email(userEmail)
                .password(rawPassword)
                .build();

        assertThatThrownBy(() ->{
            authClient.post()
                    .uri("/login")
                    .body(requestWithUserIsInactive)
                    .retrieve()
                    .toBodilessEntity();
        })
                .isInstanceOf(RestClientResponseException.class)
                .satisfies(ex ->{
                    RestClientResponseException e = (RestClientResponseException) ex;
                    assertThat(e.getStatusCode()).isEqualTo(HttpStatus.FORBIDDEN);
                    ErrorVTO errorVTO = RestClientTestUtility.parseErrorVTO(e);
                    assertThat(errorVTO.getCode()).isEqualTo(UserErrors.USER_INACTIVE.getCode());
                });
    }
}
