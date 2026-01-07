package com.ecommerce.complaints.service.rag;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.service.config.PromptAdapterConfig;
import com.ecommerce.complaints.util.HtmlSanitizer;
import com.ecommerce.complaints.util.ResourceReader;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.io.IOException;


@Service
@RequiredArgsConstructor
public class PromptBuilderService {

    private final PromptAdapterConfig promptAdapterConfig;

    public String buildAnalysisPrompt(String subject, String description,  String policyContext, String format) throws IOException {

        String analysisTemplate = ResourceReader.readAsString(promptAdapterConfig.getAnalysisTemplate());
        String classificationPrompt = ResourceReader.readAsString(promptAdapterConfig.getClassificationPrompt());
        String sentimentPrompt = ResourceReader.readAsString(promptAdapterConfig.getSentimentPrompt());
        String urgencyPrompt = ResourceReader.readAsString(promptAdapterConfig.getUrgencyPrompt());
        String keywordPrompt = ResourceReader.readAsString(promptAdapterConfig.getKeywordsPrompt());

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

        String responseTemplate = ResourceReader.readAsString(promptAdapterConfig.getResponsePrompt());
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

}