package com.ecommerce.complaints.service;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.exception.BusinessException;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.repository.api.UserRepository;
import com.ecommerce.complaints.service.api.AuthService;
import com.ecommerce.complaints.service.jwt.JwtService;
import com.ecommerce.complaints.service.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.ecommerce.complaints.model.error.UserErrors.*;


@Service
@RequiredArgsConstructor
@LogClass
public class AuthServiceImpl implements AuthService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Override
    @Transactional
    public void register(RegisterRequest request) {
        validateRegistrationRequest(request);
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new BusinessException(USER_ALREADY_EXISTS, request.getEmail());
        }
        User user = userMapper.toEntity(request);
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setRole(UserRole.CUSTOMER);
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {
        validateLoginRequest(request);

        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new BusinessException(INVALID_CREDENTIALS));

        if (!user.isActive()) {
            throw new BusinessException(USER_INACTIVE, user.getEmail());
        }

        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(INVALID_CREDENTIALS);
        }
        String token = jwtService.generateToken(user.getEmail());

        return AuthResponse.builder()
                .accessToken(token)
                .userId(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(AuthResponse.RoleEnum.valueOf(user.getRole().name()))
                .tokenType("Bearer")
                .build();
    }


    private void validateRegistrationRequest(RegisterRequest request) {
        if (request == null) {throw new BusinessException(INVALID_INPUT);}
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD, "Email");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD, "Password");
        }
        if (request.getName() == null || request.getName().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD, "Name");
        }
        if (!request.getEmail().matches("^[A-Za-z0-9+_.-]+@(.+)$")) {
            throw new BusinessException(INVALID_EMAIL, request.getEmail());
        }
        if (request.getPassword().length() < 6) {throw new BusinessException(PASSWORD_TOO_SHORT);}
    }


    private void validateLoginRequest(LoginRequest request) {
        if (request == null) {
            throw new BusinessException(INVALID_INPUT);
        }
        if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD, "Email");
        }
        if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
            throw new BusinessException(MISSING_REQUIRED_FIELD, "Password");
        }
    }
}
