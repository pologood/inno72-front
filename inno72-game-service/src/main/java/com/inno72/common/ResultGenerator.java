package com.inno72.common;

/**
 * 响应结果生成工具
 */
@SuppressWarnings("rawtypes")
public class ResultGenerator {

	public static Result genSuccessResult() {
        return Results.success();
    }

    public static <T> Result<T> genSuccessResult(T data) {
        return Results.success(data);
    }

    public static Result genFailResult(String message) {
        return Results.failure(message);
    }
}
