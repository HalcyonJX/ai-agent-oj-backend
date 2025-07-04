package com.halcyon.aiagentojbackend.model.dto.user;

import lombok.Data;

import java.io.Serializable;

/**
 * 用户编辑资料请求
 */
@Data
public class UserEditRequest implements Serializable {

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 用户简介
     */
    private String userProfile;

    private static final long serialVersionUID = 1L;
}
