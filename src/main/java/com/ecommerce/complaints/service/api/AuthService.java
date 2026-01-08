package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.vto.AuthResponse;
import com.ecommerce.complaints.model.vto.LoginRequest;
import com.ecommerce.complaints.model.vto.RegisterRequest;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
