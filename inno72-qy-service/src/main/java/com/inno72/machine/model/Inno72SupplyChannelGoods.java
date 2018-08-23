package com.inno72.machine.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

public class Inno72SupplyChannelGoods {

    /**
     * 主键
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 货道ID
     */
    @Column(name="supply_channel_id")
    private String supplyChannelId;

    /**
     * 商品ID
     */
    @Column(name="goods_id")
    private String goodsId;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplyChannelId() {
        return supplyChannelId;
    }

    public void setSupplyChannelId(String supplyChannelId) {
        this.supplyChannelId = supplyChannelId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

}
