package com.datalife.datalife.exception;

/**
 * Created by LG on 2018/1/15.
 * 自定义的服务器异常
 */

public class ServerException extends RuntimeException {
    public int code;
    public String message;

    public ServerException(int code, String message) {
        super(message);
        this.code = code;
        this.message = message;
    }
}
