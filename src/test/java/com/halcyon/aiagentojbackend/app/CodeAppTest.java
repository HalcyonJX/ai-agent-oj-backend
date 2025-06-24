package com.halcyon.aiagentojbackend.app;

import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.util.UUID;

@SpringBootTest
class CodeAppTest {

    @Resource
    private CodeApp codeApp;


    @Test
    void doChat() {
        String chatId = UUID.randomUUID().toString();
        //第一轮
        String message = "你好，我是一名正在学习Java的大三学生";
        String answer = codeApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        //第二轮
        message = "我想深入学习Java语言，你有什么建议吗?";
        answer = codeApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);
        //第三轮
        message = "我正在学习什么语言？帮我回忆一下。还有我是大几的学生，帮我回忆一下。";
        answer = codeApp.doChat(message,chatId);
        Assertions.assertNotNull(answer);
    }

    @Test
    void doChatWithReport() throws IOException {
        String chatId = UUID.randomUUID().toString();
        // 第一轮
        String message = "你好，我是程序员小张，我想学好Java，但是我不知道该怎么做";
        CodeApp.CodeReport loveReport = codeApp.doChatWithReport(message, chatId);
        Assertions.assertNotNull(loveReport);
    }
}