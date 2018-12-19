package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateConverter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "inno72_feed_back_log")
public class Inno72FeedBackLog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    @Column(name = "merchant_name")
    private String merchantName;

    @Column(name = "merchant_code")
    private String merchantCode;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "response_body")
    private String responseBody;

    /**
     * 下单时间
     */
    @Column(name = "order_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @Convert(converter = LocalDateConverter.class)
    private LocalDateTime orderTime;

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

    public LocalDateTime getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(LocalDateTime orderTime) {
        this.orderTime = orderTime;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }
}