package com.merchpilot.merchpilot.common.web;

public class RequestTransaction {
    private Object requestBody;

    public RequestTransaction() {
    }

    public RequestTransaction(Object requestBody) {
        this.requestBody = requestBody;
    }

    public Object getRequestBody() {
        return requestBody;
    }

    public void setRequestBody(Object requestBody) {
        this.requestBody = requestBody;
    }

    @Override
    public String toString() {
        return "RequestTransaction{requestBody=" + requestBody + '}';
    }
}
