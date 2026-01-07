package com.ecommerce.complaints.repository;

import com.ecommerce.complaints.model.enums.DocumentSource;
import com.ecommerce.complaints.repository.api.VectorRepository;

import com.ecommerce.complaints.util.SearchRequestFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VectorRepositoryImpl implements VectorRepository {

    private final VectorStore vectorStore;

    @Override
    public void add(List<Document> documents) {vectorStore.add(documents);}

    @Override
    public List<Document> similaritySearch(String query, int topK) {
        SearchRequest request = SearchRequestFactory.create(query, topK);
        List<Document> results = vectorStore.similaritySearch(request);
        return results;
    }

    @Override
    public List<Document> similaritySearch(SearchRequest request) {
        List<Document> results = vectorStore.similaritySearch(request);
        return results;
    }

    @Override
    public List<Document> searchBySource(String query, DocumentSource source, int topK) {
        SearchRequest request = SearchRequestFactory.createForSource(query, source, topK);
        List<Document> results = vectorStore.similaritySearch(request);
        return results;
    }

}
