package com.halcyon.aiagentojbackend.model.dto.comment;

import lombok.Data;

import java.io.Serializable;

/**
 * 评论添加请求
 */
@Data
public class CommentAddRequest implements Serializable {

    /**
     * 题目id
     */
    private Long questionId;

    /**
     * 评论内容
     */
    private String content;

    /**
     * 被评论id，用于二级评论
     */
    private Long beCommentId;

    private static final long serialVersionUID = 1L;
}
