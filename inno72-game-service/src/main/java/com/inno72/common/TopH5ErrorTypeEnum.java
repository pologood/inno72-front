package com.inno72.common;

/**
 * 聚石塔统一h5错误页面
 */
public enum TopH5ErrorTypeEnum {

	IS_SCANNED(10, "二维码已经被扫");

	Integer value;
	String name;

	TopH5ErrorTypeEnum(Integer value, String name) {
		this.value = value;
		this.name = name;
	}

	public Integer getValue() {
		return this.value;
	}

	public String getName() {
		return this.name;
	}

}
