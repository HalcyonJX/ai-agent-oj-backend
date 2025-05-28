package com.halcyon.aiagentojbackend.demo.invoke;

/**
 * 通义千问AI SDK调用示例类
 * 演示如何使用阿里云通义千问API进行AI对话生成
 */
import com.alibaba.dashscope.aigc.generation.Generation;
import com.alibaba.dashscope.aigc.generation.GenerationParam;
import com.alibaba.dashscope.aigc.generation.GenerationResult;
import com.alibaba.dashscope.common.Message;
import com.alibaba.dashscope.common.Role;
import com.alibaba.dashscope.exception.ApiException;
import com.alibaba.dashscope.exception.InputRequiredException;
import com.alibaba.dashscope.exception.NoApiKeyException;
import com.alibaba.dashscope.utils.JsonUtils;

import java.util.Arrays;

public class SdkAiInvoke {

    /**
     * 调用通义千问AI进行对话生成
     * 
     * @return GenerationResult AI生成的结果
     * @throws NoApiKeyException API密钥未设置或无效时抛出
     * @throws InputRequiredException 输入参数缺失或无效时抛出
     */
    public static GenerationResult callWithMessage() throws NoApiKeyException, InputRequiredException {
        // 创建通义千问生成服务实例
        Generation gen = new Generation();

        // 构建系统角色消息，设置AI助手的基本行为
        Message systemMsg = Message.builder()
                .role(Role.SYSTEM.getValue())
                .content("You are a helpful assistant")
                .build();

        // 构建用户消息，设置用户的具体问题
        Message userMsg = Message.builder()
                .role(Role.USER.getValue())
                .content("你是谁？")
                .build();

        // 构建生成参数配置
        GenerationParam param = GenerationParam.builder()
                .apiKey(TestApiKey.API_KEY)        // 设置API密钥
                .model("qwen-plus")                // 使用通义千问plus模型
                .messages(Arrays.asList(systemMsg, userMsg))  // 设置对话消息列表
                .resultFormat(GenerationParam.ResultFormat.MESSAGE)  // 设置返回结果格式为消息格式
                .build();

        // 调用AI服务并返回生成结果
        return gen.call(param);
    }
    /**
     * 主方法：演示如何调用通义千问AI服务并处理结果
     * 
     * @param args 命令行参数（未使用）
     */
    public static void main(String[] args) {
        try {
            // 调用AI服务获取生成结果
            GenerationResult result = callWithMessage();
            // 将结果转换为JSON格式并打印
            System.out.println(JsonUtils.toJson(result));
        } catch (ApiException | NoApiKeyException | InputRequiredException e) {
            // 捕获并处理可能发生的异常：
            // - ApiException: API调用过程中的通用异常
            // - NoApiKeyException: API密钥缺失或无效
            // - InputRequiredException: 输入参数验证失败
            System.err.println("An error occurred while calling the generation service: " + e.getMessage());
        }
        // 正常退出程序
        System.exit(0);
    }
}
