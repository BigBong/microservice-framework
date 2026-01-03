package com.frodo.bigbong.micro.framework.common;

import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

@Builder
@Data
public class Pagination implements Serializable {

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
    @Builder.Default
    private Long totalSize = 0L;

}
