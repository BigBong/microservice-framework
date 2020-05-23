package com.frodo.bigbong.micro.framework.exception;

/**
 * 参数异常
 *
 * @author frodoking on 2019/10/25.
 */
public class ArgumentException extends RuntimeException {
    public ArgumentException(String message) {
        super(message);
    }
}
