package com.frodo.bigbong.micro.framework.response;

import lombok.*;

import java.io.Serializable;

/**
 * @author frodoking on 2019/10/18.
 */
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CommonPageRequest implements Serializable {
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
}
