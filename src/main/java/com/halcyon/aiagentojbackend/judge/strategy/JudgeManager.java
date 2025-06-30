package com.halcyon.aiagentojbackend.judge.strategy;

import com.halcyon.aiagentojbackend.judge.strategy.impl.DefaultJudgeStrategy;
import com.halcyon.aiagentojbackend.judge.strategy.model.JudgeContext;
import com.halcyon.aiagentojbackend.model.dto.question.JudgeInfo;
import com.halcyon.aiagentojbackend.model.entity.QuestionSubmit;
import org.springframework.stereotype.Service;

/**
 * 判题管理（简化调用）
 */
@Service
public class JudgeManager {

    /**
     * 执行判题
     *
     * @param judgeContext
     * @return
     */
    public JudgeInfo doJudge(JudgeContext judgeContext) {
//        QuestionSubmit questionSubmit = judgeContext.getQuestionSubmit();
//        String language = questionSubmit.getLanguage();
        JudgeStrategy judgeStrategy = new DefaultJudgeStrategy();
//        if ("java".equals(language)) {
//            judgeStrategy = new JavaLanguageJudgeStrategy();
//        }
        return judgeStrategy.doJudge(judgeContext);
    }

}