package com.ecommerce.complaints.service.api;

import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;

public interface AuthService {

    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
