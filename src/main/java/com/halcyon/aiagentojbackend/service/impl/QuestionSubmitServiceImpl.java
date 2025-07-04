package com.halcyon.aiagentojbackend.service.impl;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.judge.service.JudgeService;
import com.halcyon.aiagentojbackend.mapper.QuestionSubmitMapper;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitAddRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionSubmitQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.model.entity.QuestionSubmit;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.enums.JudgeInfoMessageEnum;
import com.halcyon.aiagentojbackend.model.enums.QuestionSubmitLanguageEnum;
import com.halcyon.aiagentojbackend.model.enums.QuestionSubmitStatusEnum;
import com.halcyon.aiagentojbackend.model.vo.QuestionSubmitVO;
import com.halcyon.aiagentojbackend.model.vo.QuestionVO;
import com.halcyon.aiagentojbackend.service.QuestionService;
import com.halcyon.aiagentojbackend.service.QuestionSubmitService;
import com.halcyon.aiagentojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
* @author 张嘉鑫
* @description 针对表【question_submit(题目提交)】的数据库操作Service实现
* @createDate 2025-05-28 16:41:51
*/
@Service
public class QuestionSubmitServiceImpl extends ServiceImpl<QuestionSubmitMapper, QuestionSubmit>
    implements QuestionSubmitService{

    @Resource
    private UserService userService;

    @Resource
    private QuestionService questionService;

    @Resource
    @Lazy
    private JudgeService judgeService;

    @Override
    public long doQuestionSubmit(QuestionSubmitAddRequest questionSubmitAddRequest, HttpServletRequest request) {
        ThrowUtils.throwIf(questionSubmitAddRequest == null, ErrorCode.PARAMS_ERROR);
        String language = questionSubmitAddRequest.getLanguage();
        QuestionSubmitLanguageEnum languageEnum = QuestionSubmitLanguageEnum.getEnumByValue(language);
        ThrowUtils.throwIf(languageEnum == null, ErrorCode.PARAMS_ERROR,"编程语言错误");
        Long questionId = questionSubmitAddRequest.getQuestionId();
        ThrowUtils.throwIf(questionId == null,ErrorCode.NOT_FOUND_ERROR,"题目id错误");
        Question question = questionService.getById(questionId);
        ThrowUtils.throwIf(question == null,ErrorCode.NOT_FOUND_ERROR,"题目步存在");
        Long userId = userService.getLoginUser(request).getId();
        ThrowUtils.throwIf(userId == null,ErrorCode.NOT_LOGIN_ERROR,"用户id错误");
        QuestionSubmit questionSubmit = new QuestionSubmit();
        questionSubmit.setQuestionId(questionId);
        questionSubmit.setUserId(userId);
        questionSubmit.setLanguage(languageEnum.getValue());
        questionSubmit.setCode(questionSubmitAddRequest.getCode());
        //设置判题初始状态
        questionSubmit.setStatus(QuestionSubmitStatusEnum.WAITING.getValue());
        questionSubmit.setJudgeInfo(JudgeInfoMessageEnum.WAITING.getValue());
        boolean save = this.save(questionSubmit);
        ThrowUtils.throwIf(!save, ErrorCode.SYSTEM_ERROR,"数据插入失败");
        Long id = questionSubmit.getId();
        //todo 执行判题服务
        CompletableFuture.runAsync(()-> judgeService.doJudge(id));
        return id;
    }

    @Override
    public QueryWrapper<QuestionSubmit> getQueryWrapper(QuestionSubmitQueryRequest questionSubmitQueryRequest) {
        QueryWrapper<QuestionSubmit> queryWrapper = new QueryWrapper<>();
        if(questionSubmitQueryRequest == null){
            return queryWrapper;
        }
        Long questionId = questionSubmitQueryRequest.getQuestionId();
        String language = questionSubmitQueryRequest.getLanguage();
        Integer status = questionSubmitQueryRequest.getStatus();
        Long userId = questionSubmitQueryRequest.getUserId();
        String sortField = questionSubmitQueryRequest.getSortField();
        String sortOrder = questionSubmitQueryRequest.getSortOrder();

        //拼接查询条件
        queryWrapper.eq(ObjUtil.isNotEmpty(questionId),"questionId",questionId);
        queryWrapper.eq(StrUtil.isNotEmpty(language),"language",language);
        queryWrapper.eq(QuestionSubmitStatusEnum.getEnumByValue(status) != null,"status",status);
        queryWrapper.eq(ObjUtil.isNotEmpty(userId),"userId",userId);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField),sortOrder.equals("ascend"), sortField);

        return queryWrapper;
    }

    @Override
    public QuestionSubmitVO getQuestionSubmitVO(QuestionSubmit questionSubmit, User loginUser) {
        QuestionSubmitVO questionSubmitVO = QuestionSubmitVO.objToVo(questionSubmit);
        //脱敏：只有本人或者管理员能查看到自己提交的代码
        long userId = loginUser.getId();
        if(userId != questionSubmit.getUserId() && userService.isAdmin(loginUser)){
            questionSubmitVO.setCode("******");
        }
        return questionSubmitVO;
    }

    @Override
    public Page<QuestionSubmitVO> getQuestionSubmitVOPage(Page<QuestionSubmit> questionSubmitPage, User loginUser) {
        List<QuestionSubmit> questionSubmitList = questionSubmitPage.getRecords();
        Page<QuestionSubmitVO> questionSubmitVOPage = new Page<>(questionSubmitPage.getCurrent(),questionSubmitPage.getSize(),questionSubmitPage.getTotal());
        if(CollectionUtil.isEmpty(questionSubmitList)){
            return questionSubmitVOPage;
        }
        List<QuestionSubmitVO> questionSubmitVOList = questionSubmitList.stream()
                .map(questionSubmit -> getQuestionSubmitVO(questionSubmit, loginUser))
                .collect(Collectors.toList());
        questionSubmitVOPage.setRecords(questionSubmitVOList);
        return questionSubmitVOPage;
    }
}




