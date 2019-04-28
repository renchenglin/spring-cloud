package com.example.validation.web.advice;

public class ResultInfo<T> {
    private int code;
    private String message;
    private T body;

    public ResultInfo(int code, String message, T body) {
        this.code = code;
        this.message = message;
        this.body = body;
    }

    public ResultInfo(int code, String message) {
        this(code, message, null);
    }

    public ResultInfo(String message) {
        this(200, message, null);
    }

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public T getBody() {
		return body;
	}

	public void setBody(T body) {
		this.body = body;
	}
    
    
}
