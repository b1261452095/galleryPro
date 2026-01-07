package com.tt.ttpictureserver.common;

import lombok.Data;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description: 分页请求包装
 * @date 2026-01-07 15:21
 */

@Data
public class PageRequest {

    /**
     * 当前页号
     */

    private int current = 1;

    /**
     * 页面大小
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序(默认降序)
     */
    private String sortOrder = "desc";
}
