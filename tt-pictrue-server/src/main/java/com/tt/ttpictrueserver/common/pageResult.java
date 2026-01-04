package com.tt.ttpictrueserver.common;

import lombok.Data;

@Data
public class pageResult {

    /**
     * 当前页号
     */
    private int pageNumber = 1;

    /**
     * 页面数量
     */
    private int pageSize = 10;

    /**
     * 排序字段
     */
    private String sortField;

    /**
     * 排序顺序（默认升序）
     */
    private String sortOrder = "des";
}
