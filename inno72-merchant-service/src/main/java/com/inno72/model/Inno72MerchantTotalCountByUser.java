package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_merchant_total_count_by_user")
public class Inno72MerchantTotalCountByUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String date;

    private String city;

    /**
     * 关注用户数量
     */
    @Column(name = "concern_num")
    private Integer concernNum;

    private Integer uv;

    @Column(name = "merchant_id")
    private String merchantId;

    @Column(name = "seller_id")
    private String sellerId;

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
     * @return date
     */
    public String getDate() {
        return date;
    }

    /**
     * @param date
     */
    public void setDate(String date) {
        this.date = date;
    }

    /**
     * @return city
     */
    public String getCity() {
        return city;
    }

    /**
     * @param city
     */
    public void setCity(String city) {
        this.city = city;
    }

    /**
     * 获取关注用户数量
     *
     * @return concern_num - 关注用户数量
     */
    public Integer getConcernNum() {
        return concernNum;
    }

    /**
     * 设置关注用户数量
     *
     * @param concernNum 关注用户数量
     */
    public void setConcernNum(Integer concernNum) {
        this.concernNum = concernNum;
    }

    /**
     * @return uv
     */
    public Integer getUv() {
        return uv;
    }

    /**
     * @param uv
     */
    public void setUv(Integer uv) {
        this.uv = uv;
    }

    /**
     * @return merchant_id
     */
    public String getMerchantId() {
        return merchantId;
    }

    /**
     * @param merchantId
     */
    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    /**
     * @return seller_id
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * @param sellerId
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}