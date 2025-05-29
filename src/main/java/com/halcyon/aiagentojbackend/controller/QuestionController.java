package com.halcyon.aiagentojbackend.controller;

import com.halcyon.aiagentojbackend.common.BaseResponse;
import com.halcyon.aiagentojbackend.common.ResultUtils;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionAddRequest;
import com.halcyon.aiagentojbackend.service.QuestionService;
import com.halcyon.aiagentojbackend.service.QuestionSubmitService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * 题目相关接口
 */
@RestController
@Slf4j
@RequestMapping("/question")
public class QuestionController {

    @Resource
    private QuestionService questionService;

    @Resource
    private QuestionSubmitService questionSubmitService;

    /**
     * 题目创建接口
     * @param questionAddRequest
     * @param request
     * @return
     */
    @PostMapping("/add")
    public BaseResponse<Long> addQuestion(@RequestBody QuestionAddRequest questionAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR);
        ThrowUtils.throwIf(request == null, ErrorCode.NOT_LOGIN_ERROR);
        long questionId = questionService.addQuestion(questionAddRequest, request);
        return ResultUtils.success(questionId);
    }
}
