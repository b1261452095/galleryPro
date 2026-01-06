package com.tt.ttpictureserver.model.user.domain.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 用户注册请求参数
 * @date 2026-01-06 15:28
 */
@Data
@ApiModel("用户登录请求参数")
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true, example = "admin")
    private String userAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "admin123456")
    private String password;
}
