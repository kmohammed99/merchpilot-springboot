package com.merchpilot.merchpilot.exception;

public class NotFoundException extends ApiException {
    public NotFoundException(String message) { super(40400, message); }
}
