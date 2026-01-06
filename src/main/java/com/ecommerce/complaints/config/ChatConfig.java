package com.ecommerce.complaints.config;


import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.transformers.TransformersEmbeddingModel;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.pgvector.PgVectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class ChatConfig {

    @Value("${spring.ai.vectorstore.pgvector.dimensions:384}")
    private int dimensions;

    @Value("${spring.ai.vectorstore.pgvector.distance-type:COSINE_DISTANCE}")
    private String distanceType;

    @Value("${spring.ai.vectorstore.pgvector.index-type:HNSW}")
    private String indexType;

    @Value("${spring.ai.vectorstore.pgvector.initialize-schema:true}")
    private boolean initializeSchema;

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

}
