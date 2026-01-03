package com.frodo.bigbong.micro.framework.common;

import com.google.common.collect.Lists;

import java.io.Serializable;
import java.util.List;

/**
 * @author frodoking on 2019/10/18.
 */
public class RpcPageResponse<T> extends RpcResponse<PaginationData<T>> implements Serializable {

    public static <T> RpcPageResponse<T> warp(Integer code, String message, PaginationData<T> page) {
        RpcPageResponse <T> res = new RpcPageResponse<>();
        res.setCode(code);
        res.setMessage(message);
        res.setData(page);
        return res;
    }

    public static <T> RpcPageResponse<T> warp(Integer code, String message, Integer page, Integer pageSize,
                                              Long totalSize, List<T> data) {
        PaginationData<T> paginationData = PaginationData.<T>builder()
                .list(data)
                .pagination(Pagination.builder()
                        .page(page)
                        .pageSize(pageSize)
                        .total(totalSize).build())
                .build();

        if (page != null && pageSize != null && totalSize != null) {
            Integer totalPages = (int) ((totalSize + pageSize - 1) / pageSize);
            paginationData.getPagination().setTotalPages(totalPages);
        }

        return warp(code, message, paginationData);
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

    public static <T> RpcPageResponse<T> success(Integer page, Integer pageSize, Long totalSize, List<T> data) {
        return warp(RpcResponse.SUCCESS, "success", page, pageSize, totalSize, data);
    }

    public static <T> RpcPageResponse<T> success(String message, List<T> data) {
        return warp(RpcResponse.SUCCESS, message, 0, 20, 0L, data);
    }

    public static <T> RpcPageResponse<T> wrapError(String message) {
        return wrapError(RpcResponse.ERROR, message);
    }

    public static <T> RpcPageResponse<T> wrapError(Integer code, String message) {
        return warp(code, message, 0, 20, 0L, Lists.newArrayList());
    }
}
