package com.frodo.bigbong.micro.framework.common;

/**
 * @author: frodoking
 * @date: 2020/05/08
 * @description: 多级订阅器
 */
public interface MultiSubscriber<H, R> {
    void onNext(H host, R response);

    void onError(H host, Throwable error);

    void onCompleted(H host);
}
