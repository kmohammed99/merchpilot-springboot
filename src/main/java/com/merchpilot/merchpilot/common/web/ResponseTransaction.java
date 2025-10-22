package com.merchpilot.merchpilot.common.web;

public class ResponseTransaction {
    public static final int SUCCESS_STATUS = 100;
    public static final String SUCCESS_MESSAGE = "Success";

    private int status;
    private String message;
    private String cause;       // optional on failures
    private Object responseBody;

    public ResponseTransaction() {
    }

    public ResponseTransaction(int status, String message, Object body) {
        this.status = status;
        this.message = message;
        this.responseBody = body;
    }

    public ResponseTransaction(int status, String message, String cause) {
        this.status = status;
        this.message = message;
        this.cause = cause;
    }

    public static ResponseTransaction buildSuccessResponse(Object body) {
        return new ResponseTransaction(SUCCESS_STATUS, SUCCESS_MESSAGE, body);
    }

    public static ResponseTransaction buildSuccessResponse(Object body, String msg) {
        return new ResponseTransaction(SUCCESS_STATUS, msg, body);
    }

    public static ResponseTransaction buildFailure(int code, String msg, String cause) {
        return new ResponseTransaction(code, msg, cause);
    }

    // getters/setters
    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getCause() {
        return cause;
    }

    public void setCause(String cause) {
        this.cause = cause;
    }

    public Object getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(Object responseBody) {
        this.responseBody = responseBody;
    }
}
