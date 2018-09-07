package com.inno72.common;

public enum StandardLoginTypeEnum {

	ALIBABA(0, "阿里巴巴"), NOLOGIN(1, "无需登陆");

	Integer value;
	String name;

	StandardLoginTypeEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

	public static boolean isExist(Integer value) {

		if (ALIBABA.getValue().equals(value) || NOLOGIN.getValue().equals(value)) {
			return true;
		}
		return false;
	}
}
