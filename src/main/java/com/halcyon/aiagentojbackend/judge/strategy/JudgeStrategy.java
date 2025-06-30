package com.halcyon.aiagentojbackend.judge.strategy;

import com.halcyon.aiagentojbackend.judge.strategy.model.JudgeContext;
import com.halcyon.aiagentojbackend.model.dto.question.JudgeInfo;

/**
 * 判题策略
 */
public interface JudgeStrategy {
    /**
     * 执行判题
     * @param judgeContext
     * @return
     */
    JudgeInfo doJudge(JudgeContext judgeContext);
}
