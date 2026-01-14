package com.tt.ttpictureserver.enums;

import lombok.Getter;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 权限枚举类
 * @date 2026-01-05 16:54
 */
@Getter
public enum UserRoleEnum {

    USER("user", "普通用户"),
    ADMIN("admin", "管理员");

    // 定义属性
    private final String code;
    private final String desc;

    // 构造方法
    UserRoleEnum(String code, String desc) {
        this.code = code;
        this.desc = desc;
    }

}
