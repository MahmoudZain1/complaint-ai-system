package com.ecommerce.complaints.service.rag;

import com.ecommerce.complaints.service.config.PromptAdapterConfig;
import com.ecommerce.complaints.util.HtmlSanitizer;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;


@Service
@RequiredArgsConstructor
public class PromptBuilderService {

    private final PromptAdapterConfig promptAdapterConfig;

    public String buildAnalysisPrompt(String subject, String description,  String policyContext, String format) throws IOException {

        String analysisTemplate =readAsString(promptAdapterConfig.getAnalysisTemplate());
        String classificationPrompt =readAsString(promptAdapterConfig.getClassificationPrompt());
        String sentimentPrompt = readAsString(promptAdapterConfig.getSentimentPrompt());
        String urgencyPrompt =readAsString(promptAdapterConfig.getUrgencyPrompt());
        String keywordPrompt = readAsString(promptAdapterConfig.getKeywordsPrompt());

        String prompt = analysisTemplate
                .replace("{subject}", subject != null ? subject : "")
                .replace("{content}", description != null ? description : "")
                .replace("{classificationPrompt}", classificationPrompt)
                .replace("{sentimentPrompt}", sentimentPrompt)
                .replace("{urgencyPrompt}", urgencyPrompt)
                .replace("{keywordPrompt}", keywordPrompt)
                .replace("{policyContext}", policyContext != null ? policyContext : "")
                .replace("{format}", format);

        return prompt;
    }

    public String buildResponsePrompt(String subject, String description,
                                      Long complaintId, String format , String policiesContext, String similarComplaintsContext) throws IOException {

        String responseTemplate = readAsString(promptAdapterConfig.getResponsePrompt());
        String safeSubject = HtmlSanitizer.escapeHtml(subject);
        String safeDescription = HtmlSanitizer.escapeHtml(description);

        String prompt = responseTemplate
                .replace("{complaintId}", complaintId.toString())
                .replace("{complaintSubject}", safeSubject)
                .replace("{complaintDescription}", safeDescription)
                .replace("{policiesContext}", policiesContext != null ? policiesContext : "")
                .replace("{similarComplaintsContext}", similarComplaintsContext != null ? similarComplaintsContext : "")
                .replace("{format}", format);

        return prompt;
    }


    public static String readAsString(Resource resource) throws IOException {
        if (resource == null || !resource.exists()) {
            throw new IOException("");
        }
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8
        );
    }

}