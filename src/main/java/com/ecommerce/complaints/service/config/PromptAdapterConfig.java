package com.ecommerce.complaints.service.config;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;

@Data
@Configuration
@AllArgsConstructor
@NoArgsConstructor
public class PromptAdapterConfig {

    @Value("classpath:prompts/analysis-template.prompt")
    private Resource analysisTemplate;

    @Value("classpath:prompts/classification.prompt")
    private Resource classificationPrompt;

    @Value("classpath:prompts/sentiment-analysis.prompt")
    private Resource sentimentPrompt;

    @Value("classpath:prompts/urgency-detection.prompt")
    private Resource urgencyPrompt;

    @Value("classpath:prompts/keyword-extraction.prompt")
    private Resource keywordsPrompt;

    @Value("classpath:prompts/Response.prompt")
    private Resource responsePrompt;

}
