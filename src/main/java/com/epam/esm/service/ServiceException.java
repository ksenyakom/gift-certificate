package com.epam.esm.service;

public class ServiceException extends Exception{
    private int errorCode;

    public int getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(int errorCode) {
        this.errorCode = errorCode;
    }

    public ServiceException(int errorCode) {
    }

    public ServiceException(String message,int errorCode) {
        super(message);
    }

    public ServiceException(String message, Throwable cause, int errorCode) {
        super(message, cause);
    }

    public ServiceException(Throwable cause, int errorCode) {
        super(cause);
    }
}
