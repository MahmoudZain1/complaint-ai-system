package com.ecommerce.complaints.service.config;


import com.ecommerce.complaints.config.YamlPropertySourceFactory;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.Resource;

@Configuration
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix ="prompt")
@PropertySource(value = "classpath:config/prompt_adapter.yaml" , factory = YamlPropertySourceFactory.class)
public class PromptAdapterConfig {
    private Resource analysisTemplate;
    private Resource classificationPrompt;
    private Resource sentimentPrompt;
    private Resource urgencyPrompt;
    private Resource keywordsPrompt;
    private Resource responsePrompt;
    private Resource rewriteQuery;
}
