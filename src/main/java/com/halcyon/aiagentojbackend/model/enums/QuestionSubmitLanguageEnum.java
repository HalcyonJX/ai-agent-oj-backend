package com.halcyon.aiagentojbackend.model.enums;

import cn.hutool.core.util.ObjUtil;
import lombok.Getter;

/**
 * 编程语言枚举类
 */
@Getter
public enum QuestionSubmitLanguageEnum {
    JAVA("java","java"),
    CPP("cpp","cpp"),
    GOLANG("golang","golang");

    private final String value;

    private final String text;

    QuestionSubmitLanguageEnum(String value, String text) {
        this.value = value;
        this.text = text;
    }

    /**
     * 根据value获取枚举
     * @param value
     * @return
     */
    public static QuestionSubmitLanguageEnum getEnumByValue(String value) {
        if(ObjUtil.isEmpty(value)){
            return null;
        }
        for (QuestionSubmitLanguageEnum questionSubmitLanguageEnum : QuestionSubmitLanguageEnum.values()) {
            if (questionSubmitLanguageEnum.getValue().equals(value)) {
                return questionSubmitLanguageEnum;
            }
        }
        return null;
    }
}
