package com.ecommerce.complaints.service;

import com.ecommerce.complaints.service.config.PoliciesAdapterConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.reader.TextReader;
import org.springframework.ai.document.Document;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class EtlService implements CommandLineRunner {

    private final VectorStore vectorStore;
    private final PoliciesAdapterConfig policiesAdapterConfig;


    @Override
    public void run(String... args) throws Exception{

        TextReader reader1 = new TextReader(policiesAdapterConfig.getDeliveryPolicy());
        List<Document> docs1 = reader1.read();
        TextReader reader2 = new TextReader(policiesAdapterConfig.getReturnPolicy());
        List<Document> docs2 = reader2.read();

        TokenTextSplitter splitter =  TokenTextSplitter.builder()
                .withChunkSize(1024)
                .withMinChunkLengthToEmbed(128)
                .withMaxNumChunks(10000)
                .withMinChunkSizeChars(200)
                .build();
        List<Document> chunks = splitter.apply(List.of(docs1, docs2).stream().flatMap(List::stream).toList());

        vectorStore.add(chunks);

    }
}
