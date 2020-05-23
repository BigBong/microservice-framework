package com.frodo.bigbong.micro.framework.common;

import com.frodo.bigbong.micro.framework.exception.ArgumentException;

import java.util.List;


public class MicroService {

    public void check(String argumentName, Boolean flag) {
        if (flag != null && flag) {
            throw new ArgumentException("参数" + argumentName + "不能正确");
        }
    }

    public void checkNull(String argumentName, Object object) {
        if (object == null) {
            throw new ArgumentException("参数" + argumentName + "不能为空");
        }
    }

    public void checkEmpty(String argumentName, List list) {
        checkNull(argumentName, list);
        if (list.isEmpty()) {
            throw new ArgumentException("参数" + argumentName + "列表内容不能为空");
        }
    }

    public void assertMin(String argumentName, Long min, Long value) {
        if (value < min) {
            throw new ArgumentException("参数" + argumentName + "不能小于" + min + ",当前" + value);
        }
    }

    public void assertMax(String argumentName, Long max, Long value) {
        if (value > max) {
            throw new ArgumentException("参数" + argumentName + "不能大于" + max + ",当前" + value);
        }
    }

    public void assertRange(String argumentName, Long start, Long end, Long value) {
        if (value < start || value > end) {
            throw new ArgumentException("参数" + argumentName + "不能超过 [" +
                    start + "," + end + "] 范围" + ",当前" + value);
        }
    }
}
