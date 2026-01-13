package com.ecommerce.complaints.config;


import com.ecommerce.complaints.ai.advisor.SensitiveWordsAdvisor;
import com.ecommerce.complaints.ai.tools.CustomerTools;
import com.ecommerce.complaints.service.config.PromptAdapterConfig;
import lombok.RequiredArgsConstructor;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.chat.prompt.PromptTemplate;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.rag.preretrieval.query.transformation.RewriteQueryTransformer;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Set;


@Configuration
@RequiredArgsConstructor
public class AiConfig {

    @Value("${spring.ai.vectorstore.pgvector.dimensions:384}")
    private int dimensions;

    @Value("${spring.ai.vectorstore.pgvector.distance-type:COSINE_DISTANCE}")
    private String distanceType;

    @Value("${spring.ai.vectorstore.pgvector.index-type:HNSW}")
    private String indexType;

    @Value("${spring.ai.vectorstore.pgvector.initialize-schema:true}")
    private boolean initializeSchema;

    private final PromptAdapterConfig promptAdapterConfig;

    @Bean
    public EmbeddingModel embeddingModel() {
        return new TransformersEmbeddingModel();
    }

    @Bean
    public VectorStore vectorStore(EmbeddingModel embeddingModel, JdbcTemplate jdbcTemplate) {
        return PgVectorStore.builder(jdbcTemplate, embeddingModel)
                .schemaName("public")
                .vectorTableName("vector_store")
                .dimensions(dimensions)
                .distanceType(PgVectorStore.PgDistanceType.valueOf(distanceType))
                .indexType(PgVectorStore.PgIndexType.valueOf(indexType))
                .initializeSchema(initializeSchema)
                .build();
    }

    @Bean
    public SensitiveWordsAdvisor sensitiveWordsAdvisor() {
        Set<String> blockedWords = Set.of(
                "system override", "ignore all", "you are now", "act as",
                "instead of", "write me a",
                "hack", "inject", "sql", "select", "delete", "drop");
        String message = "I cannot process requests containing restricted content. Please rephrase.";
        return new SensitiveWordsAdvisor(blockedWords, message);
    }


    @Bean
    public ChatClient chatClient(ChatClient.Builder chatClientBuilder,
                                 VectorStore vectorStore,
                                 CustomerTools customerTools,
                                 SensitiveWordsAdvisor sensitiveWordsAdvisor) {
        return chatClientBuilder
                .defaultAdvisors(
                        sensitiveWordsAdvisor,
                        new QuestionAnswerAdvisor(vectorStore))
                .defaultTools(customerTools)
                .build();
    }
    @Bean
    public RewriteQueryTransformer queryTransformer(ChatClient.Builder chatClientBuilder) throws IOException {
        Resource resource = promptAdapterConfig.getRewriteQuery();
        String promptTemplateString = StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
        PromptTemplate promptTemplate = new PromptTemplate(promptTemplateString);
        return new RewriteQueryTransformer(chatClientBuilder, promptTemplate, "query");
    }

}
