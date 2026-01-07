package com.tt.ttpictureserver.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 删除请求包装类
 * @date 2026-01-07 15:27
 */
@Data
public class DeleteRequest implements Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * id
     */
    private Long id;

}
