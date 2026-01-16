package com.ecommerce.complaints.common;

import com.ecommerce.complaints.model.generate.AuthResponse;
import com.ecommerce.complaints.model.generate.LoginRequest;
import org.springframework.web.client.RestClient;


public class TestAuthUtils {


    public static AuthResponse loginAsCustomer(RestClient authClient) {

        LoginRequest loginRequest = new LoginRequest(
                TestDataFactory.DEFAULT_EMAIL,
                TestDataFactory.DEFAULT_PASSWORD
        );
       AuthResponse authResponse = authClient.post()
                .uri("/login")
                .body(loginRequest)
                .retrieve()
                .body(AuthResponse.class);

        return authResponse;
}

    public static AuthResponse loginAsManager(RestClient authClient) {
        LoginRequest loginRequest = new LoginRequest(
                TestDataFactory.DEFAULT_MANAGER_EMAIL,
                TestDataFactory.DEFAULT_MANAGER_PASSWORD
        );
        AuthResponse authResponse = authClient.post()
                .uri("/login")
                .body(loginRequest)
                .retrieve()
                .body(AuthResponse.class);

        return authResponse;
    }


}
