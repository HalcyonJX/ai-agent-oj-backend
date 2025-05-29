package com.halcyon.aiagentojbackend.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.ObjUtil;
import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.google.gson.Gson;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.model.dto.question.JudgeCase;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionAddRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.vo.QuestionVO;
import com.halcyon.aiagentojbackend.model.vo.UserVO;
import com.halcyon.aiagentojbackend.service.QuestionService;
import com.halcyon.aiagentojbackend.mapper.QuestionMapper;
import com.halcyon.aiagentojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
* @author 张嘉鑫
* @description 针对表【question(题目)】的数据库操作Service实现
* @createDate 2025-05-28 16:41:45
*/
@Service
public class QuestionServiceImpl extends ServiceImpl<QuestionMapper, Question>
    implements QuestionService{

    @Resource
    private UserService userService;

    private final static Gson GSON = new Gson();

    @Override
    public long addQuestion(QuestionAddRequest questionAddRequest,HttpServletRequest request) {
        ThrowUtils.throwIf(questionAddRequest == null, ErrorCode.PARAMS_ERROR,"参数不能为空");
        Question question = new Question();
        BeanUtil.copyProperties(questionAddRequest,question);
        List<String> tagList = questionAddRequest.getTags();
        if(tagList != null){
            question.setTags(GSON.toJson(tagList));
        }
        List<JudgeCase> judgeCaseList = questionAddRequest.getJudgeCase();
        if(judgeCaseList != null){
            question.setJudgeCase(GSON.toJson(judgeCaseList));
        }
        User loginUser = userService.getLoginUser(request);
        Long userId = loginUser.getId();
        question.setUserId(userId);
        question.setFavourNum(0);
        question.setThumbNum(0);
        boolean result = this.save(question);
        ThrowUtils.throwIf(!result, ErrorCode.OPERATION_ERROR,"添加题目失败");
        return question.getId();
    }

    @Override
    public QueryWrapper<Question> getQueryWrapper(QuestionQueryRequest questionQueryRequest) {
        QueryWrapper<Question> queryWrapper = new QueryWrapper<>();
        if(questionQueryRequest == null){
            return queryWrapper;
        }
        Long id = questionQueryRequest.getId();
        String title = questionQueryRequest.getTitle();
        String answer = questionQueryRequest.getAnswer();
        List<String> tags = questionQueryRequest.getTags();
        String content = questionQueryRequest.getContent();
        Long userId = questionQueryRequest.getUserId();
        String sortField = questionQueryRequest.getSortField();
        String sortOrder = questionQueryRequest.getSortOrder();

        //拼接查询条件
        queryWrapper.like(StrUtil.isNotBlank(title), "title", title);
        queryWrapper.like(StrUtil.isNotBlank(answer), "answer", answer);
        queryWrapper.like(StrUtil.isNotBlank(content),"content",content);
        if(CollectionUtil.isNotEmpty(tags)){
            for(String tag : tags){
                queryWrapper.like("tags","\""+tag+"\"");
            }
        }
        queryWrapper.eq(ObjUtil.isNotEmpty(userId), "user_id", userId);
        queryWrapper.eq(ObjUtil.isNotEmpty(id),"id",id);
        queryWrapper.eq("isDelete",false);
        queryWrapper.orderBy(StrUtil.isNotBlank(sortField),sortOrder.equals("ascend"), sortField);
        return queryWrapper;
    }

    @Override
    public QuestionVO getQuestionVO(Question question) {
        QuestionVO questionVO = QuestionVO.objToVo(question);
        //关联查询用户信息
        Long userId = question.getUserId();
        User user = null;
        if(userId != null && userId > 0){
            user = userService.getById(userId);
        }
        UserVO userVO = userService.getUserVO(user);
        questionVO.setUserVO(userVO);
        return questionVO;
    }

    @Override
    public Page<QuestionVO> getQuestionVOPage(Page<Question> questionPage) {
        Page<QuestionVO> questionVOPage = new Page<>(questionPage.getCurrent(),questionPage.getSize(),questionPage.getTotal());
        List<Question> questionList = questionPage.getRecords();
        if(questionList == null){
            return questionVOPage;
        }
        //关联查询用户信息
        Set<Long> userIdSet = questionList.stream()
                .map(Question::getUserId)
                .collect(Collectors.toSet());
        Map<Long, List<User>> userIdUserListMap = userService.listByIds(userIdSet).stream()
                .collect(Collectors.groupingBy(User::getId));
        //填充信息
        List<QuestionVO> questionVOList = questionList.stream().map(
                question -> {
                    QuestionVO questionVO = QuestionVO.objToVo(question);
                    Long userId = question.getUserId();
                    User user = null;
                    if (userIdUserListMap.containsKey(userId)) {
                        user = userIdUserListMap.get(userId).get(0);
                    }
                    questionVO.setUserVO(userService.getUserVO(user));
                    return questionVO;
                }).collect(Collectors.toList());
        questionVOPage.setRecords(questionVOList);
        return questionVOPage;
    }
}




