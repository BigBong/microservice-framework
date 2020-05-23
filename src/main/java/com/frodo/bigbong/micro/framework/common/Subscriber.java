package com.frodo.bigbong.micro.framework.common;

/**
 * @author: frodoking
 * @date: 2020/05/07
 * @description: 很简单回调订阅器
 */
public interface Subscriber<T> {
    void onNext(T data);

    void onError(Throwable error);

    void onCompleted();
}
