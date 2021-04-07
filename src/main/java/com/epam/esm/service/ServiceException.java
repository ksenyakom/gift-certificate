package com.epam.esm.service;

public class ServiceException extends Exception{
    private String errorCode;

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(String message,String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public ServiceException(String message, String errorCode, Throwable cause) {
        super(message, cause);
        this.errorCode = errorCode;
    }

    public ServiceException(Throwable cause, String errorCode) {
        super(cause);
        this.errorCode = errorCode;
    }
}
