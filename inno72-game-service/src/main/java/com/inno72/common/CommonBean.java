package com.inno72.common;

public class CommonBean {

	public static String getSessionKey(String session){
		return SESSION_KEY+session;
	}
	public static final String SESSION_KEY = "session:";
	public static final Integer SESSION_EX = 1600;

}
