package com.frodo.bigbong.micro.framework.exception;

/**
 * @author frodoking on 2019/10/18.
 */
public class BizException extends RuntimeException {

    /**
     * 错误编码
     **/
    protected Integer errorCode;

    public BizException(Integer errorCode, String message) {
        super(message);
        this.errorCode = errorCode;
    }

    public BizException(Integer errorCode, String message, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public Integer getErrorCode() {
        return errorCode;
    }
}
