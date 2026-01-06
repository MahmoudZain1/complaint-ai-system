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

    @Value("classpath:/policies/Delivery Policy.txt")
    private Resource deliveryPolicy;

    @Value("classpath:/policies/return-policy.txt")
    private Resource returnPolicy;
}
