package com.halcyon.aiagentojbackend.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitAddRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.QuestionSubmit;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.vo.QuestionSubmitVO;
import com.halcyon.aiagentojbackend.service.QuestionSubmitService;
import com.halcyon.aiagentojbackend.mapper.QuestionSubmitMapper;
import org.springframework.stereotype.Service;

/**
* @author 张嘉鑫
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2025-05-28 16:41:51
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest) {
        return 0;
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        return null;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        return null;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        return null;
    }
}




