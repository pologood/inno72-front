package com.inno72.vo;

public class Results<T> {
    public Results() {
    }

    public static <T> Result<T> success(T data) {
        Result<T> result = new Result();
        result.setCode(Result.SUCCESS);
        result.setData(data);
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> success() {
        Result<T> result = new Result();
        result.setCode(Result.SUCCESS);
        result.setMsg("成功");
        return result;
    }

    public static <T> Result<T> failure(String msg) {
        Result<T> result = new Result();
        result.setCode(Result.FAILURE);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> warn(String msg, int code) {
        Result<T> result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static <T> Result<T> warn(String msg, int code, T data) {
        Result<T> result = new Result();
        result.setCode(code);
        result.setMsg(msg);
        result.setData(data);
        return result;
    }
}