package com.tt.ttpictureserver.exception;

public class ThrowUtils {
    // 私有构造器，防止实例化
    private ThrowUtils() {
        throw new UnsupportedOperationException("工具类不能实例化");
    }
    
    /**
     * 条件成立则抛异常
     *     // 避免 null 问题，boolean 更安全

     * @param condition
     * @param exception
     */
    public static void throwIf(boolean condition , RuntimeException exception) {
        if (condition) {
            throw exception;
        }
    }

    /**
     * 条件成立抛出异常封装
     * @param condition
     * @param errorCode
     */
    public static void throwIf(boolean condition , ErrorCode errorCode) {
        throwIf(condition, new BusinessException(errorCode));
    }


    /**
     * 条件成立抛出异常封装
     * @param isTrue 条件
     * @param errorCode 错误码
     * @param message 错误信息
     */
    public static void throwIf(boolean condition , ErrorCode errorCode,String message) {
        throwIf(condition, new BusinessException(errorCode,message));
    }

     /**
     * 条件不成立抛出异常
     * @param errorCode
     */
    public static void throwIfNot(boolean condition, ErrorCode errorCode) {
        throwIf(!condition, errorCode);
    }


    /**
     * 条件不成立抛出异常
     * @param errorCode
     */
    public static void throwIfNot(boolean condition, ErrorCode errorCode,String message) {
        throwIf(!condition, errorCode, message);
    }
    
}
