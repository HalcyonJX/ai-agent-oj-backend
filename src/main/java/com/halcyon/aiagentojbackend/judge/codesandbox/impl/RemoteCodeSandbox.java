package com.halcyon.aiagentojbackend.judge.codesandbox.impl;

import com.halcyon.aiagentojbackend.judge.codesandbox.CodeSandbox;
import com.halcyon.aiagentojbackend.judge.codesandbox.model.ExecuteCodeRequest;
import com.halcyon.aiagentojbackend.judge.codesandbox.model.ExecuteCodeResponse;

/**
 * 远程代码沙箱
 */
public class RemoteCodeSandbox implements CodeSandbox {
    @Override
    public ExecuteCodeResponse executeCode(ExecuteCodeRequest executeCodeRequest) {
        System.out.println("远程代码沙箱");
        return null;
    }
}
