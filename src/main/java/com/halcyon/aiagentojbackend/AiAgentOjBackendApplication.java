package com.halcyon.aiagentojbackend;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.ai.autoconfigure.vectorstore.pgvector.PgVectorStoreAutoConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@SpringBootApplication(exclude = PgVectorStoreAutoConfiguration.class)
@MapperScan("com.halcyon.aiagentojbackend.mapper")
@EnableAspectJAutoProxy(exposeProxy = true)
public class AiAgentOjBackendApplication {

    public static void main(String[] args) {
        SpringApplication.run(AiAgentOjBackendApplication.class, args);
    }

}
