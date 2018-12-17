package com.inno72.vo;

import java.io.Serializable;

public class Inno72AuthInfo implements Serializable {
    private static final long serialVersionUID = -5512967478698728604L;
    private String phone;
    private String channelType;
    private String phoneModel;//手机型号
    private String scanSoftware;//扫描软件
    private Integer operatingSystem;//操作系统 0ios 1 android

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

    public String getPhoneModel() {
        return phoneModel;
    }

    public void setPhoneModel(String phoneModel) {
        this.phoneModel = phoneModel;
    }

    public Integer getOperatingSystem() {
        return operatingSystem;
    }

    public void setOperatingSystem(Integer operatingSystem) {
        this.operatingSystem = operatingSystem;
    }

    public String getScanSoftware() {
        return scanSoftware;
    }

    public void setScanSoftware(String scanSoftware) {
        this.scanSoftware = scanSoftware;
    }
}
