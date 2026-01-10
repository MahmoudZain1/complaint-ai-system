package com.ecommerce.complaints.integration.common;

import com.ecommerce.complaints.model.generate.ErrorVTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.http.client.ClientHttpResponse;

import java.io.IOException;


@TestComponent
public class RestClientTestUtility {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.findAndRegisterModules();
    }

    public static ErrorVTO readErrorVTO(ClientHttpResponse response) {
        try {
            return OBJECT_MAPPER.readValue(response.getBody(), ErrorVTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read error response", e);
        }
    }
}