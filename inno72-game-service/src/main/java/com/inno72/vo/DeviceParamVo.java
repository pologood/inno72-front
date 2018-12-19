package com.inno72.vo;

import java.io.Serializable;
import java.util.List;

/**
 * 保存设备vo
 */
public class DeviceParamVo implements Serializable {


    private static final long serialVersionUID = -2852323206217654897L;

    private String sessionKey;

    private List<DeviceVo> list;

    public String getSessionKey() {
        return sessionKey;
    }

    public void setSessionKey(String sessionKey) {
        this.sessionKey = sessionKey;
    }

    public List<DeviceVo> getList() {
        return list;
    }

    public void setList(List<DeviceVo> list) {
        this.list = list;
    }
}
