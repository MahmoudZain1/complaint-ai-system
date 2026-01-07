package com.ecommerce.complaints.service.rag;



import com.ecommerce.complaints.config.aspect.annotation.LogClass;
import com.ecommerce.complaints.model.enums.DocumentSource;
import com.ecommerce.complaints.repository.api.VectorRepository;
import com.ecommerce.complaints.service.config.PoliciesAdapterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Service
@RequiredArgsConstructor
public class VectorIndexingService implements CommandLineRunner {

    private final VectorRepository vectorRepository;
    private final PoliciesAdapterConfig policiesAdapterConfig;

    private static final int CHUNK_SIZE = 1024;
    private static final int MIN_CHUNK_LENGTH = 128;
    private static final int MAX_NUM_CHUNKS = 10000;
    private static final int MIN_CHUNK_SIZE_CHARS = 200;

    @Override
    public void run(String... args) throws Exception {

        List<Document> deliveryDocs = readDocument(policiesAdapterConfig.getDeliveryPolicy(), "Delivery Policy");

        List<Document> returnDocs = readDocument(policiesAdapterConfig.getReturnPolicy(), "Return Policy");

        List<Document> allDocs = List.of(deliveryDocs, returnDocs)
                .stream().flatMap(List::stream).toList();

        List<Document> docsWithMetadata = allDocs.stream().map(doc -> {
            Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
            metadata.put("source", DocumentSource.POLICY.getCode());
            metadata.put("indexed_at", System.currentTimeMillis());
            return new Document(doc.getText(), metadata);
        }).toList();

            TokenTextSplitter splitter = TokenTextSplitter.builder()
                    .withChunkSize(CHUNK_SIZE)
                    .withMinChunkLengthToEmbed(MIN_CHUNK_LENGTH)
                    .withMaxNumChunks(MAX_NUM_CHUNKS)
                    .withMinChunkSizeChars(MIN_CHUNK_SIZE_CHARS)
                    .withKeepSeparator(true)
                    .build();
        List<Document> chunks = splitter.apply(docsWithMetadata);
        vectorRepository.add(chunks);

        }


    private List<Document> readDocument(Resource resource, String name) {
            TextReader reader = new TextReader(resource);
            List<Document> docs = reader.read();

        return docs.stream().map(doc -> {
            Map<String, Object> metadata = new HashMap<>(doc.getMetadata());
            return new Document(doc.getText(), metadata);
        }).toList();
    }


    public void indexDocument(String content, DocumentSource source, Map<String, Object> metadata) {

        Map<String, Object> fullMetadata = new HashMap<>(metadata);
        fullMetadata.put("source", source.getCode());
        fullMetadata.put("indexed_at", System.currentTimeMillis());
        Document document = new Document(content, fullMetadata);
        TokenTextSplitter splitter = TokenTextSplitter.builder()
                .withChunkSize(CHUNK_SIZE)
                .withMinChunkLengthToEmbed(MIN_CHUNK_LENGTH)
                .build();
        List<Document> chunks = splitter.apply(List.of(document));
        vectorRepository.add(chunks);
    }
}