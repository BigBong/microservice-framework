package com.frodo.bigbong.micro.framework.sal;

/**
 * @author: frodoking
 * @date: 2020/02/10
 * @description:
 */
public interface SalResponse<R> {
    boolean isOk();

    String getRMessage();

    R getRData();

    Integer getRCode();
}
