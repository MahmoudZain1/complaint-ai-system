package com.ecommerce.complaints.repository.api;

import com.ecommerce.complaints.model.enums.DocumentSource;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;

import java.util.List;

public interface VectorRepository {
    void add(List<Document> documents);
    List<Document> similaritySearch(String query, int topK);
    List<Document> similaritySearch(SearchRequest request);
    List<Document> searchBySource(String query, DocumentSource source, int topK);

}
