package com.halcyon.aiagentojbackend.model.dto.comment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.halcyon.aiagentojbackend.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;
import java.util.List;

/**
 * 评论查询请求
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class CommentQueryRequest extends PageRequest implements Serializable {

    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Long id;

    /**
     * 用户id，用于关联用户表
     */
    private Long userId;

    /**
     * 题目id，用于关联题目表
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

    /**
     * 点赞数
     */
    private Integer thumbNum;

    /**
     * 回复数
     */
    private Integer replyNum;

    private static final long serialVersionUID = 1L;
}
