package com.datalife.datalife.exception;

/**
 * Created by LG on 2018/1/30.
 */

public class ApiException1 extends RuntimeException {

    private int code;
    private String errorMsg;

    public ApiException1(int code, String msg) {
        super(msg);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
