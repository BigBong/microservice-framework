package com.frodo.bigbong.micro.framework.concurrent;

import java.util.List;

/**
 * @author: frodoking
 * @date: 2019/12/18
 * @description:
 */
public interface SplitCallable<T, R> {
    List<R> run(List<T> group);
}

