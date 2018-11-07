package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_merchant_total_count_by_order")
public class Inno72MerchantTotalCountByOrder {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    private String date;

    private String city;

    @Column(name = "goods_id")
    private String goodsId;

    @Column(name = "goods_name")
    private String goodsName;

    @Column(name = "merchant_id")
    private String merchantId;

    /**
     * 订单总数（按商品）
     */
    @Column(name = "order_qty_total")
    private Integer orderQtyTotal;

    /**
     * 成功订单数
     */
    @Column(name = "order_qty_succ")
    private Integer orderQtySucc;

    /**
     * 派发商品数量
     */
    @Column(name = "goods_num")
    private Integer goodsNum;

    @Column(name = "coupon_num")
    private Integer couponNum;

    private Integer pv;

    private Integer uv;

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
     * @return goods_name
     */
    public String getGoodsName() {
        return goodsName;
    }

    /**
     * @param goodsName
     */
    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
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
     * 获取订单总数（按商品）
     *
     * @return order_qty_total - 订单总数（按商品）
     */
    public Integer getOrderQtyTotal() {
        return orderQtyTotal;
    }

    /**
     * 设置订单总数（按商品）
     *
     * @param orderQtyTotal 订单总数（按商品）
     */
    public void setOrderQtyTotal(Integer orderQtyTotal) {
        this.orderQtyTotal = orderQtyTotal;
    }

    /**
     * 获取成功订单数
     *
     * @return order_qty_succ - 成功订单数
     */
    public Integer getOrderQtySucc() {
        return orderQtySucc;
    }

    /**
     * 设置成功订单数
     *
     * @param orderQtySucc 成功订单数
     */
    public void setOrderQtySucc(Integer orderQtySucc) {
        this.orderQtySucc = orderQtySucc;
    }

    /**
     * 获取派发商品数量
     *
     * @return goods_num - 派发商品数量
     */
    public Integer getGoodsNum() {
        return goodsNum;
    }

    /**
     * 设置派发商品数量
     *
     * @param goodsNum 派发商品数量
     */
    public void setGoodsNum(Integer goodsNum) {
        this.goodsNum = goodsNum;
    }

    /**
     * @return coupon_num
     */
    public Integer getCouponNum() {
        return couponNum;
    }

    /**
     * @param couponNum
     */
    public void setCouponNum(Integer couponNum) {
        this.couponNum = couponNum;
    }

    /**
     * @return pv
     */
    public Integer getPv() {
        return pv;
    }

    /**
     * @param pv
     */
    public void setPv(Integer pv) {
        this.pv = pv;
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