package com.frodo.bigbong.micro.framework.concurrent;

/**
 * @author: frodoking
 * @date: 2019/12/19
 * @description:
 */
public interface JoinCallable<V> {
    String key();

    V call();
}
