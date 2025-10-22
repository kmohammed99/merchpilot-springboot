package com.merchpilot.merchpilot.exception;

public abstract class ApiException extends RuntimeException {
    private final int code;          // كود منطقي داخلي (مثلاً 40001)
    public ApiException(int code, String message) {
        super(message); this.code = code;
    }
    public int getCode() { return code; }
}
