package com.ecommerce.complaints.service.rag;

import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.model.enums.DocumentSource;
import com.ecommerce.complaints.repository.api.VectorRepository;
import com.ecommerce.complaints.util.SearchRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RAGContextService {

    private final VectorRepository vectorRepository;

    public List<Document> retrievePolicyContext(String complaintText) {
        SearchRequest request = SearchRequestFactory.createForPolicies(complaintText);
        return vectorRepository.similaritySearch(request);
    }

    public List<Document> retrieveSimilarComplaints(String complaintText) {
        SearchRequest request = SearchRequestFactory.createForComplaints(complaintText);
        return vectorRepository.similaritySearch(request);
    }

    public List<Document> retrieveFromSource(String query, DocumentSource source, int topK) {
        return vectorRepository.searchBySource(query, source, topK);
    }

    public String buildContextString(List<Document> documents) {
        StringBuilder context = new StringBuilder();

        for (int i = 0; i < documents.size(); i++) {
            Document doc = documents.get(i);
            context.append(String.format("[Document %d]\n", i + 1));

            if (doc.getMetadata().containsKey("source")) {
                context.append("Source: ")
                        .append(doc.getMetadata().get("source"))
                        .append("\n");
            }
            if (doc.getText() != null) {
                context.append(doc.getText()).append("\n\n");
            } else if (doc.getMedia() != null) {
                context.append("[Media: ").append(doc.getMedia().getMimeType()).append("]\n\n");
            }

            context.append("---\n\n");
        }

        return context.toString();
    }

    public String getPolicyContextForResponse(String complaintText) {
        List<Document> policies = retrievePolicyContext(complaintText);
        return buildContextString(policies);
    }

    public String getRelevantContext(String query, int topK) {
        List<Document> documents = vectorRepository.similaritySearch(query, topK);
        return documents.stream()
                .filter(doc -> doc.getText() != null)
                .map(Document::getText)
                .collect(Collectors.joining("\n\n"));
    }


}