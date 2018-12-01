package com.example.drools.domain;

import lombok.Data;

/**
 * Created by Ares on 2018/6/23.
 */
@Data
public class Message {
    public static final Integer HELLO = 0;
    public static final Integer GOODBYE = 1;

    private String message;

    private Integer status;

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public Integer getStatus() {
		return status;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}
    
    
}
