package com.inno72.vo;

import java.io.Serializable;

/**
 * 设备vo
 */
public class DeviceVo implements Serializable {
    private static final long serialVersionUID = -6173949652090126098L;
    /**
     * 设备名称e
     */
    private String deviceName;
    /**
     * 设备编码
     */
    private String machineCode;
    /**
     * 商品id
     */
    private String goodsId;

    private String storeName;

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getStoreName() {
        return storeName;
    }

    public void setStoreName(String storeName) {
        this.storeName = storeName;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
