package com.merchpilot.merchpilot.exception;

public class BusinessException extends ApiException {
    public BusinessException(String message) {
        super(40000, message);
    }
}
