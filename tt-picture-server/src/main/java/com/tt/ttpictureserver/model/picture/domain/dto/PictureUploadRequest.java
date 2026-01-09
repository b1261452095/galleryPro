package com.tt.ttpictureserver.model.picture.domain.dto;

import lombok.Data;

import java.io.Serializable;

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

    private static final long serialVersionUID = 1L;

}
