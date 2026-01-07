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
public class UserRegisterRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 账号
     */
    @ApiModelProperty(value = "账号", required = true, example = "lat")
    private String userAccount;

    /**
     * 密码
     */
    @ApiModelProperty(value = "密码", required = true, example = "123456")
    private String password;

    /**
     * 确认密码
     */
    @ApiModelProperty(value = "确认密码", required = true, example = "123456")
    private String surePassword;

}
