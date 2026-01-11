package com.ecommerce.complaints.controller;

import com.ecommerce.complaints.controller.api.AuthApi;
import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;
import com.ecommerce.complaints.service.api.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthApiImpl implements AuthApi {

    private final AuthService service;
    @Override
    public ResponseEntity<AuthResponse> login(LoginRequest loginRequest) {
        AuthResponse response = service.login(loginRequest);
        return ResponseEntity.ok(response);
    }

    @Override
    public ResponseEntity register(RegisterRequest registerRequest) {
        service.register(registerRequest);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }
}
