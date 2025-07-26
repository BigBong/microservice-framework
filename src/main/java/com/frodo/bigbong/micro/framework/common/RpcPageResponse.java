package com.frodo.bigbong.micro.framework.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.beans.BeanUtils;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author frodoking on 2019/10/18.
 */
@EqualsAndHashCode(callSuper = true)
@AllArgsConstructor
@NoArgsConstructor
@Data
@SuperBuilder
public class RpcPageResponse<T> extends Page<T> implements Serializable {
    /**
     * 的状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    public boolean isOk() {
        return code == RpcResponse.SUCCESS;
    }

    public static <T> RpcPageResponse<T> warp(Integer code, String message, Page<T> data) {
        RpcPageResponse<T> response = RpcPageResponse.<T>builder().code(code).message(message).build();
        BeanUtils.copyProperties(data, response);
        return response;
    }

    public static <T> RpcPageResponse<T> warp(Integer code, String message, List<T> data) {
        return warp(code, message, Page.<T>builder().data(data).build());
    }

    public static <T> RpcPageResponse<T> success(List<T> data) {
        return warp(RpcResponse.SUCCESS, "success", data);
    }

    public static <T> RpcPageResponse<T> success(Page<T> page) {
        return warp(RpcResponse.SUCCESS, "success", page);
    }

    public static <T> RpcPageResponse<T> success(Integer pageNum, Integer pageSize, Long totalSize, List<T> data) {
        RpcPageResponse<T> response = RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message("success").build();
        response.setData(data);
        response.setPageNum(pageNum);
        response.setPageSize(pageSize);
        response.setTotalSize(totalSize);
        return response;
    }

    public static <T> RpcPageResponse<T> success(String message, List<T> data) {
        return RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message(message).data(data).build();
    }

    public static <T> RpcPageResponse<T> success(String message, Integer pageNum, Integer pageSize,
                                                 Long totalSize, List<T> data) {
        return RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message(message).pageNum(pageNum)
                .pageSize(pageSize).totalSize(totalSize).data(data).build();
    }


    public static <T> RpcPageResponse<T> error(String message) {
        return RpcPageResponse.<T>builder().code(RpcResponse.ERROR).message(message).data(Collections.emptyList()).build();
    }

    public static <T> RpcPageResponse<T> error(Integer code, String message) {
        return RpcPageResponse.<T>builder().code(code).message(message).data(Collections.emptyList()).build();
    }
}
