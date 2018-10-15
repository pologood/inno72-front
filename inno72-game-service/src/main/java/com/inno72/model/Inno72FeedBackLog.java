package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_feed_back_log")
public class Inno72FeedBackLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "response_body")
    private String responseBody;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * @return merchant_name
     */
    public String getMerchantName() {
        return merchantName;
    }

    /**
     * @param merchantName
     */
    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    /**
     * @return goods_id
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * @param goodsId
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * @return order_id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getResponseBody() {
        return responseBody;
    }

    public void setResponseBody(String responseBody) {
        this.responseBody = responseBody;
    }
}