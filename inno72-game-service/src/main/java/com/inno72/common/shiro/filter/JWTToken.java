package com.inno72.common.shiro.filter;

import org.apache.shiro.authc.AuthenticationToken;

@SuppressWarnings("serial")
public class JWTToken implements AuthenticationToken {

	// 密钥
	private String token;

	public JWTToken(String token) {
		this.token = token;
	}

	@Override
	public Object getPrincipal() {
		return token;
	}

	@Override
	public Object getCredentials() {
		return token;
	}
}
