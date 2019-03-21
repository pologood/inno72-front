package com.inno72.common;

public enum StandardLoginTypeEnum {

	ALIBABA(0, "ALI"), NOLOGIN(1, "无需登陆"), WEIXIN(2, "WEIXIN"), INNO72(3, "INNO72"), JD(4, "JD");//伪类型

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
		StandardLoginTypeEnum[] values = StandardLoginTypeEnum.values();
		for (StandardLoginTypeEnum v : values) {
			Integer value1 = v.getValue();
			if (value.equals(value1)) {
				return true;
			}
		}

		return false;
	}

	public static String getValue(int value) {
		StandardLoginTypeEnum[] operTypeEnums = values();
		for (StandardLoginTypeEnum operTypeEnum : operTypeEnums) {
			if (operTypeEnum.getValue() == value) {
				return operTypeEnum.getName();
			}
		}
		return null;
	}
}
