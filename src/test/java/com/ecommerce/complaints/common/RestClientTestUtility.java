package com.ecommerce.complaints.common;

import com.ecommerce.complaints.model.generate.ErrorVTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.test.context.TestComponent;
import org.springframework.web.client.RestClientResponseException;

import java.io.IOException;


@TestComponent
public class RestClientTestUtility {

    private static final ObjectMapper OBJECT_MAPPER;

    static {
        OBJECT_MAPPER = new ObjectMapper();
        OBJECT_MAPPER.registerModule(new JavaTimeModule());
        OBJECT_MAPPER.findAndRegisterModules();
    }

    public static ErrorVTO parseErrorVTO(RestClientResponseException ex) {
        try {
            return OBJECT_MAPPER.readValue(ex.getResponseBodyAsString(), ErrorVTO.class);
        } catch (IOException e) {
            throw new RuntimeException("Failed to read error response", e);
        }
    }
}