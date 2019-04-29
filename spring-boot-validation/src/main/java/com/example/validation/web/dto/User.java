package com.example.validation.web.dto;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import com.example.validation.web.validator.CaseMode;
import com.example.validation.web.validator.CheckCase;


public class User {
	@Size(min = 6,max = 16)
    @NotNull(message = "{userId}{javax.validation.constraints.NotNull.message}")
    private String userId;
	
	@CheckCase(value = CaseMode.LOWER,message = "{userName}")
	private String userNameLower;

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public String getUserNameLower() {
		return userNameLower;
	}

	public void setUserNameLower(String userNameLower) {
		this.userNameLower = userNameLower;
	}



	
	
    
}
