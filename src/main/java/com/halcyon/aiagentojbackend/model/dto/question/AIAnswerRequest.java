package com.halcyon.aiagentojbackend.model.dto.question;

import lombok.Data;

import java.io.Serializable;

/**
 * AI答题请求类
 */
@Data
public class AIAnswerRequest implements Serializable {

    private Long userId;

    private Long questionId;

    private String language;

    private static final long serialVersionUID = 1L;
}
