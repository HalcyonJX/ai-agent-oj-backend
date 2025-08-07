package com.halcyon.aiagentojbackend.model.vo;

import cn.hutool.json.JSONUtil;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.model.dto.question.JudgeCase;
import com.halcyon.aiagentojbackend.model.entity.Question;
import lombok.Data;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.List;

/**
 * AI生成题目信息
 */
@Data
public class AiGeneratedQuestionVO implements Serializable {

    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目内容
     */
    private String content;

    /**
     * 难度等级（0-简单，1-中等，2-困难）
     */
    private Integer difficulty;

    /**
     * 示例代码(JAVA)
     */
    private String sampleCode;

    /**
     * 判题用例
     */
    private List<JudgeCase> judgeCase;

    //生成题目转为question
    public static Question toQuestion(AiGeneratedQuestionVO aiGeneratedQuestionVO,List<String> tags,Long userId){
        ThrowUtils.throwIf(aiGeneratedQuestionVO == null, ErrorCode.PARAMS_ERROR);
        Question question = new Question();
        BeanUtils.copyProperties(aiGeneratedQuestionVO,question);
        question.setAnswer(aiGeneratedQuestionVO.getSampleCode());
        question.setUserId(userId);
        if(tags != null){
            question.setTags(JSONUtil.toJsonStr(tags));
        }
        //设置判题用例
        List<JudgeCase> judgeCaseList = aiGeneratedQuestionVO.getJudgeCase();
        if(judgeCaseList != null && !judgeCaseList.isEmpty()){
            question.setJudgeCase(JSONUtil.toJsonStr(judgeCaseList));
        }
        return question;
    }

    private static final long serialVersionUID = 1L;
}
