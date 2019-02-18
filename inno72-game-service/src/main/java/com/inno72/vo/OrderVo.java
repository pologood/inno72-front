package com.inno72.vo;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * 订单返回vo
 */
public class OrderVo implements Serializable {

    private static final long serialVersionUID = -1886588458556512664L;

    private String id;//订单id
    private String orderNum;//订单号
    private Date orderTime;//下单时间
    private String orderTimeStr;
    private String goodsName;//商品名称
    private BigDecimal orderPrice;//订单金额
    private Integer orderStatus;//订单状态编码
    private String orderStatusName;//订单状态名称
    private Date refundCreateTime;
    private Date refundTime;
    private String refundCreateTimeStr;//退款提交时间
    private String refundTimeStr;//退款时间
    private Integer refundStatus;//退款状态 状态：0 新退款订单，1退款中，2退款成功，3退款失败
    private String refundStatusStr;
    private String refundNum;//退款编号
    private String reason;//退款说明
    private Integer buttonFlag;//按钮状态：0显示退款，1退款灰掉，2显示查看详情

    public enum REFUND_STATUS {

        NEW(0, "新退款订单"),INREFUND(1, "退款中"),REFUND_SUCCESS(2, "退款成功"),REFUND_FAIL(3, "退款失败");

        private Integer key;
        private String desc;

        REFUND_STATUS(Integer key, String desc) {
            this.key = key;
            this.desc = desc;
        }

        public Integer getKey() {
            return key;
        }

        public void setKey(Integer key) {
            this.key = key;
        }

        public String getDesc() {
            return desc;
        }

        public void setDesc(String desc) {
            this.desc = desc;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    public Date getOrderTime() {
        return orderTime;
    }

    public void setOrderTime(Date orderTime) {
        this.orderTime = orderTime;
    }

    public String getOrderTimeStr() {
        return orderTimeStr;
    }

    public void setOrderTimeStr(String orderTimeStr) {
        this.orderTimeStr = orderTimeStr;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public BigDecimal getOrderPrice() {
        return orderPrice;
    }

    public void setOrderPrice(BigDecimal orderPrice) {
        this.orderPrice = orderPrice;
    }

    public Integer getOrderStatus() {
        return orderStatus;
    }

    public void setOrderStatus(Integer orderStatus) {
        this.orderStatus = orderStatus;
    }

    public String getOrderStatusName() {
        return orderStatusName;
    }

    public void setOrderStatusName(String orderStatusName) {
        this.orderStatusName = orderStatusName;
    }

    public Date getRefundCreateTime() {
        return refundCreateTime;
    }

    public void setRefundCreateTime(Date refundCreateTime) {
        this.refundCreateTime = refundCreateTime;
    }

    public Date getRefundTime() {
        return refundTime;
    }

    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }

    public String getRefundCreateTimeStr() {
        return refundCreateTimeStr;
    }

    public void setRefundCreateTimeStr(String refundCreateTimeStr) {
        this.refundCreateTimeStr = refundCreateTimeStr;
    }

    public String getRefundTimeStr() {
        return refundTimeStr;
    }

    public Integer getRefundStatus() {
        return refundStatus;
    }

    public void setRefundStatus(Integer refundStatus) {
        this.refundStatus = refundStatus;
    }

    public void setRefundTimeStr(String refundTimeStr) {
        this.refundTimeStr = refundTimeStr;
    }

    public String getRefundNum() {
        return refundNum;
    }

    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public Integer getButtonFlag() {
        return buttonFlag;
    }

    public void setButtonFlag(Integer buttonFlag) {
        this.buttonFlag = buttonFlag;
    }

    public String getRefundStatusStr() {
        return refundStatusStr;
    }

    public void setRefundStatusStr(String refundStatusStr) {
        this.refundStatusStr = refundStatusStr;
    }
}
