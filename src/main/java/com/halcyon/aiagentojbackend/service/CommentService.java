package com.halcyon.aiagentojbackend.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentAddRequest;
import com.halcyon.aiagentojbackend.model.dto.comment.CommentQueryRequest;
import com.halcyon.aiagentojbackend.model.dto.question.QuestionQueryRequest;
import com.halcyon.aiagentojbackend.model.entity.Comment;
import com.baomidou.mybatisplus.extension.service.IService;
import com.halcyon.aiagentojbackend.model.entity.Question;
import com.halcyon.aiagentojbackend.model.vo.CommentVO;
import com.halcyon.aiagentojbackend.model.vo.QuestionVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

/**
* @author 张嘉鑫
* @description 针对表【comment(评论表)】的数据库操作Service
* @createDate 2025-07-04 09:48:55
*/
public interface CommentService extends IService<Comment> {

    /**
     * 添加评论
     * @param commentAddRequest
     * @return
     */
    long addComment(CommentAddRequest commentAddRequest, HttpServletRequest request);

    /**
     * 根据问题Id获取评论列表
     * @param questionId
     * @return
     */
    List<CommentVO> listCommentsByQuestionId(Long questionId);

    /**
     * 获取查询条件
     * @param commentQueryRequest
     * @return
     */
    QueryWrapper<Comment> getQueryWrapper(CommentQueryRequest commentQueryRequest);

    /**
     * 分页获取评论封装
     * @param commentPage
     * @return
     */
    Page<CommentVO> getCommentVOPage(Page<Comment> commentPage);

    /**
     * 获取评论的回复列表
     * @param commentId 评论ID
     * @return 回复列表
     */
    List<CommentVO> listCommentReplies(long commentId);

    /**
     * 点赞评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean likeComment(long commentId, long userId);

    /**
     * 删除评论
     * @param commentId 评论ID
     * @param userId 用户ID
     * @return 是否成功
     */
    boolean removeComment(long commentId, long userId);
}
