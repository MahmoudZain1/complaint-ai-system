package com.ecommerce.complaints.service.config;

import org.springframework.core.io.Resource;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class PoliciesAdapterConfig {

    @Value("classpath:/documents/Delivery-Policy.md")
    private Resource deliveryPolicy;

    @Value("classpath:/documents/return-policy.md")
    private Resource returnPolicy;
}
