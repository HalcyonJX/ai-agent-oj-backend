package com.halcyon.aiagentojbackend.judge.codesandbox;

import com.halcyon.aiagentojbackend.judge.codesandbox.impl.ExampleCodeSandbox;
import com.halcyon.aiagentojbackend.judge.codesandbox.impl.RemoteCodeSandbox;

/**
 * 代码沙箱工厂（根据字符串参数来创建指定的代码沙箱）
 */
public class CodeSandboxFactory {

    public static CodeSandbox newInstance(String type){
        switch (type){
            case "remote":
                return new RemoteCodeSandbox();
            case "example":
            default:
                return new ExampleCodeSandbox();
        }
    }
}
