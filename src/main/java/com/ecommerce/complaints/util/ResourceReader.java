package com.ecommerce.complaints.util;

import org.springframework.util.StreamUtils;
import org.springframework.core.io.Resource;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class ResourceReader {

    public static String readAsString(Resource resource) throws IOException {
        if (resource == null || !resource.exists()) {
            throw new IOException("");
        }
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8
        );
    }

}
