package com.inno72.common;

/**
 * 响应结果生成工具
 */
public class ResultGenerator {

	public static Result<String> genSuccessResult() {
		return Results.success();
	}

	public static <T> Result<T> genSuccessResult(T data) {
		return Results.success(data);
	}

	public static Result<String> genFailResult(String message) {
		return Results.failure(message);
	}
}
