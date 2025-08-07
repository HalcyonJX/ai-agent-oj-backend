package com.halcyon.aiagentojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 题目生成请求
 */
@Data
public class QuestionGenerateRequest implements Serializable {
    /**
     * 题目标题
     */
    private String title;

    /**
     * 题目标签
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;
}
