package com.halcyon.aiagentojbackend.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.halcyon.aiagentojbackend.common.BaseResponse;
import com.halcyon.aiagentojbackend.common.DeleteRequest;
import com.halcyon.aiagentojbackend.common.ResultUtils;
import com.halcyon.aiagentojbackend.exception.BusinessException;
import com.halcyon.aiagentojbackend.exception.ErrorCode;
import com.halcyon.aiagentojbackend.exception.ThrowUtils;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentAddRequest;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Comment;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.model.entity.User;
import com.halcyon.aiagentojbackend.model.vo.CommentVO;
import com.halcyon.aiagentojbackend.service.CommentService;
import com.halcyon.aiagentojbackend.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/comment")
@Slf4j
public class CommentController {
    @Resource
    private UserService userService;

    @Resource
    private CommentService commentService;

    @PostMapping("/add")
    public BaseResponse<Long> addComment(@RequestBody CommentAddRequest commentAddRequest, HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        ThrowUtils.throwIf(commentAddRequest == null,ErrorCode.PARAMS_ERROR,"参数为空");
        long l = commentService.addComment(commentAddRequest, request);
        return ResultUtils.success(l);
    }

    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteComment(@RequestBody DeleteRequest deleteRequest,HttpServletRequest request){
        ThrowUtils.throwIf(deleteRequest == null,ErrorCode.PARAMS_ERROR,"参数为空");
        Long commentId = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null,ErrorCode.NOT_LOGIN_ERROR);
        boolean res = commentService.removeComment(commentId, loginUser.getId());
        return ResultUtils.success(res);
    }

    @PostMapping("/list/page")
    public BaseResponse<Page<CommentVO>> listQuestionComments(@RequestBody CommentQueryRequest commentQueryRequest,HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);
        int current = commentQueryRequest.getCurrent();
        int size = commentQueryRequest.getPageSize();
        //限制爬虫
        ThrowUtils.throwIf(size > 20, ErrorCode.PARAMS_ERROR);
        Page<Comment> questionPage = commentService.page(new Page<>(current, size),
                commentService.getQueryWrapper(commentQueryRequest));
        return ResultUtils.success(commentService.getCommentVOPage(questionPage));
    }
    @GetMapping("/list/replies/{commentId}")
    public BaseResponse<List<CommentVO>> listCommentReplies(@PathVariable("commentId") Long commentId){
        ThrowUtils.throwIf(commentId == null || commentId <= 0,ErrorCode.PARAMS_ERROR);
        List<CommentVO> commentVOList = commentService.listCommentReplies(commentId);
        return ResultUtils.success(commentVOList);
    }
    @PostMapping("/like/{id}")
    public BaseResponse<Boolean> likeComment(@PathVariable("id") Long commentId, HttpServletRequest request) {
        ThrowUtils.throwIf(commentId == null || commentId <= 0,ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        boolean result = commentService.likeComment(commentId, loginUser.getId());
        return ResultUtils.success(result);
    }
}
