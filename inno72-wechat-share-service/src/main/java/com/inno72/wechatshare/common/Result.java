package com.inno72.wechatshare.common;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Result<T> implements Serializable {
    private static final long serialVersionUID = 2499433944948002095L;
    private int code;
    private T data;
    private String msg;
    public static int SUCCESS = 0;
    public static int FAILURE = 1;

    public Result() {
    }

    public Map<String, Object> map() {
        Map<String, Object> map = new HashMap();
        map.put("code", this.code);
        map.put("data", this.data);
        map.put("msg", this.msg);
        return map;
    }

    public boolean success() {
        return this.code == SUCCESS;
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}