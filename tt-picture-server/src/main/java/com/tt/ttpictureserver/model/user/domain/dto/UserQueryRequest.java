package com.tt.ttpictureserver.model.user.domain.dto;

import com.tt.ttpictureserver.common.PageRequest;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 用户注册请求参数
 * @date 2026-01-06 15:28
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class UserQueryRequest extends PageRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

    /**
     * 用户昵称
     */
    private String userName;

    /**
     * 用户头像
     */
    private String userAvatar;

    /**
     * 简介
     */
    private String userProfile;

    /**
     * 用户角色 user admin
     */
    private String userRole;
}
