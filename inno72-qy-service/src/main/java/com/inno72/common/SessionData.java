package com.inno72.common;

import com.inno72.check.model.Inno72CheckUser;

public class SessionData {
	private String token;
	private Inno72CheckUser user;

	public SessionData() {
		super();
	}

	public SessionData(String token, Inno72CheckUser user) {
		super();
		this.token = token;
		this.user = user;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Inno72CheckUser getUser() {
		return user;
	}

	public void setUser(Inno72CheckUser user) {
		this.user = user;
	}


}
