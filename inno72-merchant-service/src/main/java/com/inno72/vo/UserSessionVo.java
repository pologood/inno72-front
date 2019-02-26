package com.inno72.vo;

import com.inno72.model.Inno72MerchantUser;

public class UserSessionVo extends Inno72MerchantUser {
	private String token;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}


}
