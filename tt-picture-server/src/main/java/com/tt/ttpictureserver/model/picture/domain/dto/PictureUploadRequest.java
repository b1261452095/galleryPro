package com.tt.ttpictureserver.model.picture.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @author NoBug
 * @version 1.0
 * @project tt-picture-server
 * @description:
 * @date 2026-01-09 17:27
 */
@Data
public class PictureUploadRequest implements Serializable {

    /**
     * 图片id
     */
    private Long id;

    /**
     * 图片名称
     */
    private String name;

    /**
     * 简介
     */
    private String introduction;

    /**
     * 分类
     */
    private String category;

    /**
     * 标签（JSON 数组）
     */
    private List<String> tags;

    private static final long serialVersionUID = 1L;

}
