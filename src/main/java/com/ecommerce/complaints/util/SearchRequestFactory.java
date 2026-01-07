package com.ecommerce.complaints.util;

import com.ecommerce.complaints.model.enums.DocumentSource;
import org.springframework.ai.vectorstore.SearchRequest;

public class SearchRequestFactory {

    private static final double DEFAULT_SIMILARITY_THRESHOLD = 0.3;



    public static SearchRequest create(String query, int topK) {
        return SearchRequest.builder()
                .query(query).topK(topK).similarityThreshold(DEFAULT_SIMILARITY_THRESHOLD)
                .build();
    }


    public static SearchRequest createForSource(String query, DocumentSource source, int topK) {
        return SearchRequest.builder()
                .query(query)
                .topK(topK).similarityThreshold(DEFAULT_SIMILARITY_THRESHOLD)
                .filterExpression(source.getFilterExpression())
                .build();
    }


    public static SearchRequest createWithThreshold(String query, int topK, double similarityThreshold) {
        return SearchRequest.builder()
                .query(query)
                .topK(topK)
                .similarityThreshold(similarityThreshold)
                .build();
    }


    public static SearchRequest createForPolicies(String query) {
        return createForSource(query, DocumentSource.POLICY, 7);
    }

    public static SearchRequest createForComplaints(String query) {
        return createForSource(query, DocumentSource.COMPLAINT, 3);
    }
}
