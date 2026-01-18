package com.ecommerce.complaints.common;

import com.ecommerce.complaints.model.entity.Complaint;
import com.ecommerce.complaints.model.entity.ComplaintResponse;
import com.ecommerce.complaints.model.entity.User;
import com.ecommerce.complaints.model.enums.ComplaintCategory;
import com.ecommerce.complaints.model.enums.Priority;
import com.ecommerce.complaints.model.enums.Sentiment;
import com.ecommerce.complaints.model.enums.UserRole;
import com.ecommerce.complaints.model.generate.*;

import static com.ecommerce.complaints.model.enums.ResponseStatus.PENDING_APPROVAL;

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

    public static Complaint createSampleComplaint() {
        return Complaint.builder()
                .id(100L)
                .subject("Original Subject")
                .description("The product arrived broken and I am very unhappy.")
                .customer(createSampleUser())
                .customerName(DEFAULT_NAME)
                .customerEmail(DEFAULT_EMAIL)
                .build();
    }

    public static ComplaintAnalysisVTO createSampleAnalysisVTO() {
        return ComplaintAnalysisVTO.builder()
                .category(ComplaintCategory.PRODUCT)
                .priority(Priority.HIGH)
                .sentiment(Sentiment.NEGATIVE)
                .summary("The customer is complaining about a broken product.")
                .build();


    }
    public static ComplaintResponseVTO createSampleResponseVTO() {
        return ComplaintResponseVTO.builder()
                .complaintId(100L)
                .generatedResponse("This is a sample AI generated response.")
                .tone("Empathetic")
                .confidenceScore(0.95)
                .build();
    }

    public static User createSampleManager() {
        return User.builder()
                .id(2L)
                .name("Manager User")
                .email(DEFAULT_MANAGER_EMAIL)
                .role(UserRole.MANAGER)
                .active(true)
                .build();
    }

    public static ComplaintResponse createSamplePendingResponse() {
        ComplaintResponse response = new ComplaintResponse();
        response.setId(200L);
        response.setComplaint(createSampleComplaint());
        response.setStatus(PENDING_APPROVAL);
        response.setGeneratedResponse("Sample AI response");
        return response;
    }
}
