package com.halcyon.aiagentojbackend.judge.codesandbox.impl;

import com.halcyon.aiagentojbackend.judge.codesandbox.CodeSandbox;
import com.halcyon.aiagentojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.halcyon.aiagentojbackend.judge.codesandbox.model.ExecuteCodeResponse;
import com.halcyon.aiagentojbackend.model.dto.question.JudgeInfo;
import com.halcyon.aiagentojbackend.model.enums.JudgeInfoMessageEnum;
import com.halcyon.aiagentojbackend.model.enums.QuestionSubmitStatusEnum;

import java.util.List;

/**
 * 示例代码沙箱（仅为了跑通业务流程）
 */
public class ExampleCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        List<String> inputList = executeCodeRequest.getInputList();
        ExecuteCodeResponse executeCodeResponse = new ExecuteCodeResponse();
        executeCodeResponse.setOutputList(inputList);
        executeCodeResponse.setMessage("测试执行成功");
        executeCodeResponse.setStatus(QuestionSubmitStatusEnum.SUCCEED.getValue());
        JudgeInfo judgeInfo = new JudgeInfo();
        judgeInfo.setMessage(JudgeInfoMessageEnum.ACCEPTED.getText());
        executeCodeResponse.setJudgeInfo(judgeInfo);
        return executeCodeResponse;
    }
}