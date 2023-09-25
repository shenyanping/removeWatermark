package com.example.watermark.exception;

/**
 * 自定义异常
 *
 * @author ruoyi
 */
public class CustomerException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    private Integer code;

    private String message;

    public CustomerException(String message) {
        this.code = -1;
        this.message = message;
    }

    public CustomerException(String message, Integer code) {
        this.message = message;
        this.code = code;
    }

    public CustomerException(String message, Throwable e) {
        super(message, e);
        this.message = message;
    }

    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}
