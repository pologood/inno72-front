package com.inno72.common;

import com.inno72.vo.StandardPrepareLoginReqVo;

public enum StandardLoginTypeEnum {

	ALIBABA(0, "ALI"), NOLOGIN(1, "无需登陆") ,WEIXIN(2, "WEIXIN");

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

	public static String getValue(Integer value) {
		StandardLoginTypeEnum[] operTypeEnums = values();
		for (StandardLoginTypeEnum operTypeEnum : operTypeEnums) {
			if (operTypeEnum.getValue() == value) {
				return operTypeEnum.getName();
			}
		}
		return null;
	}
}
