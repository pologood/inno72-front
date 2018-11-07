package com.inno72.common.util;

import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import com.inno72.common.Result;
import com.inno72.common.Results;

public class JSR303Util {


	/**
	 * 如果返回null则表示没有错误
	 * @param objects 校验对象
	 * @return 校验错误结果。
	 */
	public static Result<String> valid(Object... objects) {
		if (null == objects) {
			return Results.failure("校验对象不能为空!");
		}
		for (Object obj:objects){
			ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
			Validator validator = factory.getValidator();
			Set<ConstraintViolation<Object>> validResult = validator.validate(obj);
			if (null != validResult && validResult.size() > 0) {
				for (ConstraintViolation<Object> constraintViolation : validResult) {
					return Results.failure(constraintViolation.getMessage());
				}
			}
		}

		return Results.success();
	}
}
