package com.ecommerce.complaints.service;

import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.common.TestDataFactory;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.error.UserErrors;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.jwt.JwtService;
import com.ecommerce.complaints.service.mapper.UserMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
@DisplayName("Auth Service Unit Tests")
public class AuthServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private UserMapper userMapper;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtService jwtService;

    @InjectMocks
    private AuthServiceImpl authService;

    private ArgumentCaptor<User> userArgumentCaptor;

    @BeforeEach
    void setUp() {
        userArgumentCaptor = ArgumentCaptor.forClass(User.class);
    }

    @Test
    void register_shouldSucceed_withValidData(){
        RegisterRequest request = TestDataFactory.createSampleRegisterRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(false);
        User mappedUser = new User();
        when(userMapper.toEntity(request)).thenReturn(mappedUser);
        when(passwordEncoder.encode(request.getPassword())).thenReturn("password");
        authService.register(request);
        verify(userRepository, times(1)).save(userArgumentCaptor.capture());
        User savedUser = userArgumentCaptor.getValue();
        assertThat(savedUser.getPassword()).isEqualTo("password");
        assertThat(savedUser.getRole()).isEqualTo(UserRole.CUSTOMER);
        verify(userRepository, times(1)).existsByEmail(request.getEmail());
        verify(passwordEncoder, times(1)).encode(request.getPassword());
    }

    @Test
    void register_shouldFail_whenEmailAlreadyExists(){
        RegisterRequest request = TestDataFactory.createSampleRegisterRequest();
        when(userRepository.existsByEmail(request.getEmail())).thenReturn(true);
        assertThatThrownBy(()-> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrors.USER_ALREADY_EXISTS.getCode());
        verify(userRepository , never()).save(any(User.class));
        verify(userRepository, times(1)).existsByEmail(request.getEmail());

    }

    @Test
    void register_shouldThrowException_whenPasswordIsTooShort(){
        RegisterRequest request = TestDataFactory.createSampleRegisterRequest();
        request.setPassword("pass");
        assertThatThrownBy(()-> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrors.PASSWORD_TOO_SHORT.getCode());

        verify(userRepository , never()).existsByEmail(anyString());
        verify(userRepository,  never()).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenEmailIsInvalid(){
        RegisterRequest request = TestDataFactory.createSampleRegisterRequest();
        request.setEmail("invaildEmail#gmail.com");
        assertThatThrownBy(()-> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode" , UserErrors.INVALID_EMAIL.getCode());
        verify(userRepository , never()).existsByEmail(anyString());
        verify(userRepository,  never()).save(any(User.class));
    }

    @Test
    void register_shouldThrowException_whenNameIsNull(){
        RegisterRequest request = TestDataFactory.createSampleRegisterRequest();
        request.setName("");
        assertThatThrownBy(()-> authService.register(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode" , UserErrors.MISSING_REQUIRED_FIELD.getCode());
        verify(userRepository , never()).existsByEmail(anyString());
        verify(userRepository,  never()).save(any(User.class));
    }


    @Test
    void login_shouldSucceed_withValidCredentials(){
        LoginRequest request = TestDataFactory.createSampleLoginRequest();
        User userFromFactory = TestDataFactory.createSampleUser();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(userFromFactory));
        when(passwordEncoder.matches(request.getPassword(), userFromFactory.getPassword())).thenReturn(true);
        when(jwtService.generateToken(request.getEmail())).thenReturn("token");

        AuthResponse response =  authService.login(request);
        assertThat(response).isNotNull();
        assertThat(response.getAccessToken()).isNotBlank();
        assertThat(response.getAccessToken()).isEqualTo("token");
        assertThat(response.getEmail()).isEqualTo(userFromFactory.getEmail());

        verify(userRepository , times(1)).findByEmail(request.getEmail());
        verify(passwordEncoder , times(1)).matches(request.getPassword(),userFromFactory.getPassword());
        verify(jwtService , times(1)).generateToken(userFromFactory.getEmail());

    }

    @Test
    void login_shouldThrowException_whenUserNotFound(){
        LoginRequest request = TestDataFactory.createSampleLoginRequest();
        when(userRepository.findByEmail(anyString())).thenReturn(Optional.empty());
        assertThatThrownBy(()-> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode" , UserErrors.INVALID_CREDENTIALS.getCode());

        verify(jwtService ,never()).generateToken(request.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());
    }

    @Test
    void login_shouldThrowException_withInvalidPassword(){
        LoginRequest request = TestDataFactory.createSampleLoginRequest();
        User userFromFactory = TestDataFactory.createSampleUser();

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(userFromFactory));
        when(passwordEncoder.matches(request.getPassword(), userFromFactory.getPassword())).thenReturn(false);

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrors.INVALID_CREDENTIALS.getCode());

        verify(jwtService ,never()).generateToken(request.getEmail());
    }
    @Test
    void login_shouldThrowException_whenUserIsInactive(){
        LoginRequest request = TestDataFactory.createSampleLoginRequest();
        User userFromFactory = TestDataFactory.createSampleUser();
        userFromFactory.setActive(false);

        when(userRepository.findByEmail(request.getEmail())).thenReturn(Optional.ofNullable(userFromFactory));

        assertThatThrownBy(() -> authService.login(request))
                .isInstanceOf(BusinessException.class)
                .hasFieldOrPropertyWithValue("errorCode", UserErrors.USER_INACTIVE.getCode());

        verify(jwtService ,never()).generateToken(request.getEmail());
        verify(passwordEncoder, never()).matches(anyString(), anyString());

    }

}
