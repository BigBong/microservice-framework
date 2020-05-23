package com.frodo.bigbong.micro.framework.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Collections;
import java.util.List;

/**
 * @author frodoking on 2019/10/18.
 */
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Data
public class CommonPageResponse<T> implements Serializable {
    /**
     * 的状态码
     */
    private Integer code;

    /**
     * 信息
     */
    private String message;

    /**
     * 页码
     */
    @Builder.Default
    private Integer pageNum = 1;

    /**
     * 页面大小
     */
    @Builder.Default
    private Integer pageSize = 20;

    /**
     * 总数
     */
    private Long totalSize;

    /**
     * 响应数据
     */
    private List<T> data;

    public boolean isOk() {
        return code == CommonResponse.SUCCESS;
    }

    public static <T> CommonPageResponse<T> warp(Integer code, String message, List<T> data) {
        return CommonPageResponse.<T>builder().code(code).message(message).data(data).build();
    }

    public static <T> CommonPageResponse<T> success(List<T> data) {
        return CommonPageResponse.<T>builder().code(CommonResponse.SUCCESS).message("success").data(data).build();
    }

    public static <T> CommonPageResponse<T> success(Integer pageNum, Integer pageSize, Long totalSize, List<T> data) {
        return CommonPageResponse.<T>builder().code(CommonResponse.SUCCESS).message("success").pageNum(pageNum)
                .pageSize(pageSize).totalSize(totalSize).data(data).build();
    }

    public static <T> CommonPageResponse<T> success(String message, List<T> data) {
        return CommonPageResponse.<T>builder().code(CommonResponse.SUCCESS).message(message).data(data).build();
    }

    public static <T> CommonPageResponse<T> success(String message, Integer pageNum, Integer pageSize,
                                                    Long totalSize, List<T> data) {
        return CommonPageResponse.<T>builder().code(CommonResponse.SUCCESS).message(message).pageNum(pageNum)
                .pageSize(pageSize).totalSize(totalSize).data(data).build();
    }


    public static <T> CommonPageResponse<T> error(String message) {
        return CommonPageResponse.<T>builder().code(CommonResponse.ERROR).message(message).data(Collections.emptyList()).build();
    }

    public static <T> CommonPageResponse<T> error(Integer code, String message) {
        return CommonPageResponse.<T>builder().code(code).message(message).data(Collections.emptyList()).build();
    }
}
