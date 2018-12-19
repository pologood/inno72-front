package com.github.binarywang.demo.wx.mp.vo;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

public class Result implements Serializable {
    private static final long serialVersionUID = 2499433944948002095L;
    private int code;
    private Object data;
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

    public String json() {
        return JSON.toJSONString(this);
    }

    public int getCode() {
        return this.code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public Object getData() {
        return this.data;
    }

    public void setData(Object data) {
        this.data = data;
    }

    public String getMsg() {
        return this.msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}