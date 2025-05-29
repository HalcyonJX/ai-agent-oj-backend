package com.halcyon.aiagentojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * 题目提交添加请求
 */
@Data
public class QuestionSubmitAddRequest implements Serializable {

    /**
     * 语言
     */
    private String language;

    /**
     * 用户代码
     */
    private String code;

    /**
     * 题目 id
     */
    private Long questionId;

    private static final long serialVersionUID = 1L;
}
