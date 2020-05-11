package com.frodo.bigbong.micro.framework.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @author frodoking on 2019/10/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommonResponse<T> implements Serializable {

    public static final int SUCCESS = 200;
    public static final int ERROR = 500;
    public static final int ERROR_ARGUMENTS = 500001;

    /**
     * 状态码
     */
    private Integer code;

    /**
     * 调试信息
     */
    private String message;

    /**
     * 响应数据
     */
    private T data;

    public boolean isOk() {
        return code == SUCCESS;
    }

    public static <T> CommonResponse<T> warp(Integer code, String message, T data) {
        return CommonResponse.<T>builder().code(code).message(message).data(data).build();
    }

    public static <T> CommonResponse<T> success(T data) {
        return CommonResponse.<T>builder().code(SUCCESS).message("success").data(data).build();
    }

    public static <T> CommonResponse<T> success(String message, T data) {
        return CommonResponse.<T>builder().code(SUCCESS).message(message).data(data).build();
    }

    public static <T> CommonResponse<T> error(String message) {
        return CommonResponse.<T>builder().code(ERROR).message(message).build();
    }

    public static <T> CommonResponse<T> error(Integer code, String message) {
        return CommonResponse.<T>builder().code(code).message(message).build();
    }

}
