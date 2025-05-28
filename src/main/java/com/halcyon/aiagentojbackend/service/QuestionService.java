package com.halcyon.aiagentojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionAddRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.baomidou.mybatisplus.extension.service.IService;
import com.halcyon.aiagentojbackend.model.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;

/**
* @author 张嘉鑫
* @description 针对表【question(题目)】的数据库操作Service
* @createDate 2025-05-28 16:41:45
*/
public interface QuestionService extends IService<Question> {

    /**
     * 添加题目
     * @param questionAddRequest
     * @return
     */
    long addQuestion(QuestionAddRequest questionAddRequest);

    /**
     * 获取查询条件
     * @param questionQueryRequest
     * @return
     */
    QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest);

    /**
     * 获取题目封装
     * @param question
     * @return
     */
    QuestionVO getQuestionVO(Question question);

    /**
     * 分页获取题目封装
     * @param questionPage
     * @param request
     * @return
     */
    Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage, HttpServletRequest request);
}
