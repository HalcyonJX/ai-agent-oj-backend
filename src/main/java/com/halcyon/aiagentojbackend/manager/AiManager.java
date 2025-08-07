package com.halcyon.aiagentojbackend.manager;

import cn.hutool.json.JSONUtil;
import com.halcyon.aiagentojbackend.exception.BusinessException;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.model.vo.AIAnswerVO;
import com.halcyon.aiagentojbackend.model.vo.AiGeneratedQuestionVO;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * AI助手
 */
@Service
@Slf4j
public class AiManager {
    private ChatClient chatClient;

    @Resource
    private ChatModel dashscopeChatModel;

    /**
     * 生成题目prompt
     */
    private static final String QUESTION_GENERATION_PROMPT = "你是一个专业的编程题目生成专家，擅长设计算法和数据结构相关的编程题目。我需要你根据给定的标题和标签生成一个完整的编程题目。\n\n" +
            "请你严格按照以下JSON格式输出（请不要输出其他任何内容，只需要输出一个有效的JSON对象）：\n" +
            "{\n" +
            "  \"title\": \"完整的题目标题\",\n" +
            "  \"content\": \"详细的题目描述，包括问题背景、输入输出格式、约束条件等\",\n" +
            "  \"difficulty\": \"难度等级，只能是0、1或2，分别代表简单、中等和困难\",\n" +
            "  \"sampleCode\": \"完整的Java示例代码，必须包含public class Main和public static void main(String[] args)方法，且能正确解决问题\",\n" +
            "  \"judgeCase\": [\n" +
            "    {\"input\": \"测试用例1的输入(不能换行)\", \"output\": \"测试用例1的期望输出\"},\n" +
            "    {\"input\": \"测试用例2的输入(不能换行)\", \"output\": \"测试用例2的期望输出\"},\n" +
            "    {\"input\": \"测试用例3的输入(不能换行)\", \"output\": \"测试用例3的期望输出\"}\n" +
            "  ]\n" +
            "}\n\n" +
            "注意事项：\n" +
            "1. 题目内容要清晰、完整，包含必要的解释和示例\n" +
            "2. 示例代码必须是完整的、可运行的Java代码，切记必须要使用ACM模式，有清晰的导包，类名统一为Main\n" +
            "3. 测试用例应该覆盖基本情况、边界情况和特殊情况\n" +
            "4. 难度评级要与实际复杂度相符：0-简单（基本算法，时间复杂度O(n)以内），1-中等（需要一定算法知识，可能是O(nlogn)复杂度），2-困难（需要复杂算法或数据结构）\n" +
            "5. 所有题目请确保有明确的答案且测试用例可以验证答案的正确性\n" +
            "6. 请不要输出多余内容，只输出JSON格式，不需要提示这是一个JSON格式等信息。";
    /**
     * AI答题prompt
     */
    private static final String PRECONDITION = "现在你是一位精通OJ竞赛题目的算法专家，接下来我会按照以下固定格式给你发送内容：\n" +
            "题目标题：\n" +
            "{该算法题的标题}\n" +
            "题目内容:\n" +
            "{该算法题的具体内容}\n" +
            "题目使用语言:\n" +
            "{解决该题目所使用的编程语言}\n" +
            "请认真根据这两部分内容，必须严格按照以下指定格式生成markdown内容（此外不要输出任何多余的开头、结尾、注释）\n" +
            "【【【【【\n" +
            "明确的代码分析，越详细越好，不要生成多余的注释(切记是ACM模式代码)\n" +
            "【【【【【\n" +
            "解答该题目对应的代码，主类必须是Main，代码必须使用ACM模式，具有清晰的导包，只需生成要求编程语言的代码\n";

    /**
     * 生成题目
     * @param title
     * @param tags
     * @return
     */
    public AiGeneratedQuestionVO generateProgrammingQuestion(String title, List<String> tags){
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(QUESTION_GENERATION_PROMPT)
                .build();
        StringBuilder message = new StringBuilder();
        message.append("题目标题：").append(title);
        message.append("\n题目标签：").append(String.join(",",tags));
        ChatResponse response = chatClient.prompt().user(message.toString()).call().chatResponse();
        String content = response.getResult().getOutput().getText();
        log.info("AI 生成题目内容: {}",content);
        try {
            //尝试解析JSON
            return JSONUtil.toBean(content,AiGeneratedQuestionVO.class);
        }catch (Exception e){
            log.error("解析AI生成题目失败",e);
            throw new BusinessException(ErrorCode.OPERATION_ERROR,"AI生成题目失败，请重试");
        }
    }

    /**
     * AI答题
     * @param title
     * @param content
     * @param language
     * @param questionId
     * @return
     */
    public AIAnswerVO getResult(String title,String content,String language,Long questionId){
        chatClient = ChatClient.builder(dashscopeChatModel)
                .defaultSystem(PRECONDITION).build();
        String message = "标题："+title+"\n内容："+content+"\n编程语言："+language;
        ChatResponse response = chatClient.prompt().user(message).call().chatResponse();
        String result = response.getResult().getOutput().getText();
        log.info("AI 答题信息：{}",result);
        String genResult = null;
        String genCode = result;
        if(result.split("【【【【【").length >= 2){
            genResult = result.split("【【【【【")[1].trim();
            genCode = result.split("【【【【【")[2].trim();
        }
        return new AIAnswerVO(genResult,genCode,questionId);
    }
}
