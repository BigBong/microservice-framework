package com.frodo.bigbong.micro.framework.common;

import com.google.common.collect.Lists;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.List;

@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Page<T> implements Serializable {

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

    /**
     * 响应数据
     */
    @Builder.Default
    private List<T> data = Lists.newArrayList();

}
