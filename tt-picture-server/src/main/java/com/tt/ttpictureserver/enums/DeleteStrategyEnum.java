package com.tt.ttpictureserver.enums;

import lombok.Getter;

/**
 * 图片删除策略枚举
 * 
 * @author NoBug
 */
@Getter
public enum DeleteStrategyEnum {
    /**
     * 立即删除 - 同时删除数据库记录和OSS文件
     */
    IMMEDIATE("立即删除"),

    /**
     * 软删除 - 标记删除，定期清理OSS文件
     */
    SOFT_DELETE("软删除"),

    /**
     * 异步删除 - 通过消息队列异步删除OSS文件
     */
    ASYNC("异步删除");

    private final String description;

    DeleteStrategyEnum(String description) {
        this.description = description;
    }
}
