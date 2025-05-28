package com.halcyon.aiagentojbackend.model.dto.question;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目创建请求类
 */
@Data
public class QuestionAddRequest implements Serializable {

    /**
     * 题目名称
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    /**
     * 题目描述
     */
    private String content;

    /**
     * 题目测试用例
     */
    private List<JudgeCase> judgeCase;

    /**
     * 题目答案
     */
    private String answer;

    private static final long serialVersionUID = 1L;
}
