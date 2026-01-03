package com.frodo.bigbong.micro.framework.common;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Builder
@Data
public class PaginationData<T> {
    private List<T> list;
    private Pagination pagination;
}
