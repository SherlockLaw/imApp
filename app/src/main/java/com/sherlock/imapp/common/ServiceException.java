package com.sherlock.imapp.common;

/**
 * Created by Administrator on 2018/5/3 0003.
 */

public class ServiceException extends RuntimeException {
    private Integer resultCode;

    public ServiceException(Integer resultCode, String message) {
        super(message);

        this.resultCode = resultCode;
    }

    public ServiceException(String message) {
        super(message);
    }

    public Integer getResultCode() {
        return resultCode;
    }

}
