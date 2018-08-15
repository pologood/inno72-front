package com.inno72.common;

import java.util.List;

import com.inno72.system.model.Inno72Function;
import com.inno72.system.model.Inno72User;

public class SessionData {
	private String token;
	private Inno72User user;
	private List<Inno72Function> functions;

	public SessionData() {
		super();
	}

	public SessionData(String token, Inno72User user, List<Inno72Function> functions) {
		super();
		this.token = token;
		this.user = user;
		this.functions = functions;
	}

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public Inno72User getUser() {
		return user;
	}

	public void setUser(Inno72User user) {
		this.user = user;
	}

	public List<Inno72Function> getFunctions() {
		return functions;
	}

	public void setFunctions(List<Inno72Function> functions) {
		this.functions = functions;
	}

}
