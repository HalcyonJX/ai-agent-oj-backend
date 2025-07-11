package com.halcyon.aiagentojbackend.model.entity;

import com.baomidou.mybatisplus.annotation.*;

import java.io.Serializable;
import java.util.Date;
import lombok.Data;

/**
 * 评论表
 * @TableName comment
 */
@TableName(value ="comment")
@Data
public class Comment implements Serializable {
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

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 更新时间
     */
    private Date updateTime;

    /**
     * 是否删除
     */
    @TableLogic
    private Integer isDelete;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}