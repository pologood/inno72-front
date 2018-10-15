package com.inno72.vo;

import java.io.Serializable;

public class OrderOrderGoodsVo implements Serializable {
    private static final long serialVersionUID = -7727561029355440027L;
    private String orderNum;
    private String taobaoOrderNum;
    private String orderId;
    private String userId;
    private String channelId;
    private String goodsId;
    private String taobaoGoodsId;
    private Integer goodsType;
    private String userNick;

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getChannelId() {
        return channelId;
    }

    public void setChannelId(String channelId) {
        this.channelId = channelId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    public Integer getGoodsType() {
        return goodsType;
    }

    public void setGoodsType(Integer goodsType) {
        this.goodsType = goodsType;
    }

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
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
}
