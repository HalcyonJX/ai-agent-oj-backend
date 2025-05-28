package com.halcyon.aiagentojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitAddRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.QuestionSubmit;
import com.baomidou.mybatisplus.extension.service.IService;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.vo.QuestionSubmitVO;

/**
* @author 张嘉鑫
* @description 针对表【question_submit(题目提交)】的数据库操作Service
* @createDate 2025-05-28 16:41:51
*/
public interface QuestionSubmitService extends IService<QuestionSubmit> {

    /**
     * 题目提交
     * @param questionSubmitAddRequest
     * @return
     */
    long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest);

    /**
     * 获取查询条件
     * @param questionSubmitQueryRequest
     * @return
     */
    QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest);

    /**
     * 获取题目封装
     * @param questionSubmit
     * @param loginUser
     * @return
     */
    QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser);

    /**
     * 分页获取题目封装
     * @param questionSubmitPage
     * @param loginUser
     * @return
     */
    Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser);
}
