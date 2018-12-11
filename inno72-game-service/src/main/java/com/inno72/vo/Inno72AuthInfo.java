package com.inno72.vo;

import java.io.Serializable;

public class Inno72AuthInfo implements Serializable {
    private static final long serialVersionUID = -5512967478698728604L;
    private String phone;
    private String channelType;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChannelType() {
        return channelType;
    }

    public void setChannelType(String channelType) {
        this.channelType = channelType;
    }
}
