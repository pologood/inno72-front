package com.inno72.validator;

import com.inno72.exception.ParameterException;

public class Validators {

	/**
	 * 校验参数不为空
	 */
	public static void checkParamNotNull(Object... params) {
		for (Object param : params) {
			if (param == null) {
				throw new ParameterException("参数不能为空");
			}
		}
	}
}
