package com.frodo.bigbong.micro.framework.concurrent;

import java.util.List;

/**
 * @author: frodoking
 * @date: 2019/12/16
 * @description:
 */
public interface SplitRunnable<T> {
    void run(List<T> group);
}
