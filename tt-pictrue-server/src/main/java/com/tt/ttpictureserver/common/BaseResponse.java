package com.tt.ttpictureserver.common;

import lombok.Data;

import java.io.Serializable;

/**
 * 通用响应类
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;
    private String message;
    private T data;

    public BaseResponse(int code, String message, T data) {
        this.code = code;
        this.message = message;
        this.data = data;
    }

    public BaseResponse(int code, String message) {
        this(code, message, null);
    }

    public static <T> BaseResponse<T> success(T data) {
        return new BaseResponse<>(0, "成功", data);
    }

    public static <T> BaseResponse<T> error(int code, String message) {
        return new BaseResponse<>(code, message);
    }
}
