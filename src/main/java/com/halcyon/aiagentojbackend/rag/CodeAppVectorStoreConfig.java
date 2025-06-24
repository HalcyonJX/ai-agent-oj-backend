package com.halcyon.aiagentojbackend.rag;

import jakarta.annotation.Resource;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SimpleVectorStore;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * 编程大师向量数据库配置（初始化基于内存的向量数据库）
 */
@Configuration
public class CodeAppVectorStoreConfig {

    @Resource
    private CodeAppDocumentLoader codeAppDocumentLoader;

    @Bean
    VectorStore codeAppVectorStore(EmbeddingModel dashscopeEmbeddingModel) {
        SimpleVectorStore simpleVectorStore = SimpleVectorStore.builder(dashscopeEmbeddingModel).build();
        List<Document> documentList = codeAppDocumentLoader.loadMarkdowns();
        simpleVectorStore.add(documentList);
        return simpleVectorStore;
    }
}
