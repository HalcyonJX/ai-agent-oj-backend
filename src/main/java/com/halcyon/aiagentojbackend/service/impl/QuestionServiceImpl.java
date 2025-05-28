package com.halcyon.aiagentojbackend.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.service.QuestionService;
import com.halcyon.aiagentojbackend.mapper.QuestionMapper;
import org.springframework.stereotype.Service;

/**
* @author 张嘉鑫
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-05-28 16:41:45
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

}




