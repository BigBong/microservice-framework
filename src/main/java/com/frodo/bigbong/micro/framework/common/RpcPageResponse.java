package com.frodo.bigbong.micro.framework.common;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

/**
 * @author frodoking on 2019/10/18.
 */
@EqualsAndHashCode(callSuper = true)
@Data
@SuperBuilder
public class RpcPageResponse<T> extends RpcResponse<PaginationData<T>> implements Serializable {

    public static <T> RpcPageResponse<T> warp(Integer code, String message, PaginationData<T> page) {
        return (RpcPageResponse<T>) RpcPageResponse.<T>builder().code(code).message(message).data(page).build();
    }

    public static <T> RpcPageResponse<T> warp(Integer code, String message, List<T> data) {
        return warp(code, message, PaginationData.<T>builder().list(data).build());
    }

    public static <T> RpcPageResponse<T> success(List<T> data) {
        return warp(RpcResponse.SUCCESS, "success", data);
    }

    public static <T> RpcPageResponse<T> success(PaginationData<T> page) {
        return warp(RpcResponse.SUCCESS, "success", page);
    }

    public static <T> RpcPageResponse<T> success(Integer pageNum, Integer pageSize, Long totalSize, List<T> data) {
        PaginationData<T> build = PaginationData.<T>builder()
                .list(data)
                .pagination(Pagination.builder()
                        .pageNum(pageNum)
                        .pageSize(pageSize)
                        .totalSize(totalSize).build())
                .build();
        return (RpcPageResponse<T>) RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message("success").data(build).build();
    }

    public static <T> RpcPageResponse<T> success(String message, List<T> data) {
        return (RpcPageResponse<T>) RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message(message).data(data).build();
    }

    public static <T> RpcPageResponse<T> success(String message, Integer pageNum, Integer pageSize,
                                                 Long totalSize, List<T> data) {
        PaginationData<T> build = PaginationData.<T>builder()
                .list(data)
                .pagination(Pagination.builder()
                        .pageNum(pageNum)
                        .pageSize(pageSize)
                        .totalSize(totalSize).build())
                .build();
        return (RpcPageResponse<T>) RpcPageResponse.<T>builder().code(RpcResponse.SUCCESS).message(message).data(build).build();
    }

    public static <T> RpcResponse<T> error(String message) {
        return error(RpcResponse.ERROR, message);
    }

    public static <T> RpcResponse<T> error(Integer code, String message) {
        PaginationData<T> build = PaginationData.<T>builder()
                .list(Lists.newArrayList())
                .pagination(Pagination.builder()
                        .pageNum(0)
                        .pageSize(20)
                        .totalSize(0L).build())
                .build();
        return RpcPageResponse.<T>builder().code(code).message(message).data(build).build();
    }
}
