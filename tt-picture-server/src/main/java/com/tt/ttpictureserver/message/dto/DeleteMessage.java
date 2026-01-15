package com.tt.ttpictureserver.message.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 图片删除消息
 * 
 * @author bianhongbin
 * @date 2026-01-15
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class DeleteMessage implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 图片ID
     */
    private Long pictureId;

    /**
     * 图片URL
     */
    private String url;

    /**
     * 删除时间
     */
    private LocalDateTime deleteTime;

    /**
     * 重试次数
     */
    private Integer retryCount;

    /**
     * 构造函数 (不包含重试次数)
     */
    public DeleteMessage(Long pictureId, String url) {
        this.pictureId = pictureId;
        this.url = url;
        this.deleteTime = LocalDateTime.now();
        this.retryCount = 0;
    }
}
