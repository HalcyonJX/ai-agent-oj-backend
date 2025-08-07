package com.halcyon.aiagentojbackend.model.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * AI答题结果
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AIAnswerVO implements Serializable {
    private String result;
    private String code;
    private Long questionId;
    private static final long serialVersionUID = 1L;
}
