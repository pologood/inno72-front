package com.framelib.common;

import java.util.List;

public class PmpSessionData {
	private String token;
	private GeneralUser user;
	private List<GeneralAuthority> firstLevelAuth;
	private List<GeneralAuthority> secondLevelAuth;
	private List<GeneralAuthority> thirdLevelAuth;
	private List<String> roleIds;

	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}

	public GeneralUser getUser() {
		return user;
	}

	public void setUser(GeneralUser user) {
		this.user = user;
	}

	public List<GeneralAuthority> getFirstLevelAuth() {
		return firstLevelAuth;
	}

	public void setFirstLevelAuth(List<GeneralAuthority> firstLevelAuth) {
		this.firstLevelAuth = firstLevelAuth;
	}

	public List<GeneralAuthority> getSecondLevelAuth() {
		return secondLevelAuth;
	}

	public void setSecondLevelAuth(List<GeneralAuthority> secondLevelAuth) {
		this.secondLevelAuth = secondLevelAuth;
	}

	public List<GeneralAuthority> getThirdLevelAuth() {
		return thirdLevelAuth;
	}

	public void setThirdLevelAuth(List<GeneralAuthority> thirdLevelAuth) {
		this.thirdLevelAuth = thirdLevelAuth;
	}

	public List<String> getRoleIds() {
		return roleIds;
	}

	public void setRoleIds(List<String> roleIds) {
		this.roleIds = roleIds;
	}

}
