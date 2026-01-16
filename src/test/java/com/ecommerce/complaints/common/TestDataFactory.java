package com.ecommerce.complaints.common;

import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.generate.ComplaintCreateDTO;
import com.ecommerce.complaints.model.generate.LoginRequest;
import com.ecommerce.complaints.model.generate.RegisterRequest;

public class TestDataFactory {

    public static final String DEFAULT_EMAIL = "zain@gmail.com";
    public static final String DEFAULT_PASSWORD = "password";
    public static final String DEFAULT_NAME = "Mahmoud Zain";

    public static final String DEFAULT_MANAGER_EMAIL = "manager@example.com";
    public static final String DEFAULT_MANAGER_PASSWORD = "password_manager";



    public static RegisterRequest createSampleRegisterRequest() {
        return new RegisterRequest(DEFAULT_NAME, DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static LoginRequest createSampleLoginRequest() {
        return new LoginRequest(DEFAULT_EMAIL, DEFAULT_PASSWORD);
    }

    public static User createSampleUser() {
        return User.builder()
                .id(1L)
                .name(DEFAULT_NAME)
                .email(DEFAULT_EMAIL)
                .password(DEFAULT_PASSWORD)
                .role(UserRole.CUSTOMER)
                .active(true)
                .build();
    }

    public static ComplaintCreateDTO createSampleComplaintDTO() {
        return ComplaintCreateDTO.builder()
                .subject("Product arrived damaged")
                .description("The box was crushed.")
                .build();
    }


}
