package com.halcyon.aiagentojbackend.app;

import com.halcyon.aiagentojbackend.advisor.MyLoggerAdvisor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.MessageChatMemoryAdvisor;
import org.springframework.ai.chat.memory.ChatMemory;
import org.springframework.ai.chat.memory.InMemoryChatMemory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_CONVERSATION_ID_KEY;
import static org.springframework.ai.chat.client.advisor.AbstractChatMemoryAdvisor.CHAT_MEMORY_RETRIEVE_SIZE_KEY;

@Component
@Slf4j
public class CodeApp {
    private final ChatClient chatClient;


    /**
     * 加载系统prompt
     */
    private String loadSystemPrompt() throws IOException {
        // 使用Spring的ClassPathResource加载文件
        Resource resource = new ClassPathResource("system-prompt.txt");
        try (InputStream inputStream = resource.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {

            // 保留原始换行格式
            return reader.lines().collect(Collectors.joining("\n"));
        }
    }

    public CodeApp(ChatModel dashscopeChatModel) throws IOException {
        // 从文件加载系统Prompt
        String systemPrompt = loadSystemPrompt();
        //初始化基于内存的对话记忆
        ChatMemory chatMemory = new InMemoryChatMemory();
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(systemPrompt)
                .defaultAdvisors(
                        new MessageChatMemoryAdvisor(chatMemory),
                        //自定义日志 Advisor
                        new MyLoggerAdvisor()
                )
                .build();
    }
    public String doChat(String message,String chatId){
        ChatResponse response = chatClient.prompt().user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY, chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY, 10))
                .call()
                .chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("content: {}",content);
        return content;
    }

    /**
     * 定义编程报告类
     * @param title
     * @param suggestions
     */
    record CodeReport(String title, List<String> suggestions){

    }

    /**
     * 编程报告功能
     * @param message
     * @param chatId
     * @return
     * @throws IOException
     */
    public CodeReport doChatWithReport(String message,String chatId) throws IOException {
        String systemPrompt = this.loadSystemPrompt();
        CodeReport codeReport = chatClient.prompt()
                .system(systemPrompt+"每次对话后都要生成编程结果，标题为{用户名}的编程报告，内容为建议列表")
                .user(message)
                .advisors(spec -> spec.param(CHAT_MEMORY_CONVERSATION_ID_KEY,chatId)
                        .param(CHAT_MEMORY_RETRIEVE_SIZE_KEY,10))
                .call()
                .entity(CodeReport.class);
        log.info("codeReport: {}",codeReport);
        return codeReport;
    }
}
