package com.inno72.vo;

import java.io.Serializable;

public class OrderOrderGoodsVo implements Serializable {
    private static final long serialVersionUID = -7727561029355440027L;
    private String taobaoOrderNum;
    private String orderId;
    private String goodsId;
    private String taobaoGoodsId;
    private String machineCode;

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }


    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }


    public String getTaobaoOrderNum() {
        return taobaoOrderNum;
    }

    public void setTaobaoOrderNum(String taobaoOrderNum) {
        this.taobaoOrderNum = taobaoOrderNum;
    }

    public String getTaobaoGoodsId() {
        return taobaoGoodsId;
    }

    public void setTaobaoGoodsId(String taobaoGoodsId) {
        this.taobaoGoodsId = taobaoGoodsId;
    }


    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}
