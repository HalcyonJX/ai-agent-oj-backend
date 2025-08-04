package com.halcyon.aiagentojbackend.agent;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
@SpringBootTest
class ManusTest {
    @Resource
    private Manus manus;

    @Test
    void run() {
        String userPrompt = """
                请给我写一个简单的动态规划的编程题目，写入文件里。
                """;
        String answer = manus.run(userPrompt);
        Assertions.assertNotNull(answer);
    }
}