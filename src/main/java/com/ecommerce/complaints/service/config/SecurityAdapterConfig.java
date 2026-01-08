package com.ecommerce.complaints.service.config;

import com.ecommerce.complaints.config.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix ="jwt")
@PropertySource(value = "classpath:config/security_adapter.yaml" , factory = YamlPropertySourceFactory.class)
public class SecurityAdapterConfig {
    private String secret;
    private Long expiration;
}
