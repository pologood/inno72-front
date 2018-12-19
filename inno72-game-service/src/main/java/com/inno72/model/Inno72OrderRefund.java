package com.inno72.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inno72_order_refund")
public class Inno72OrderRefund {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 订单id
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 手机号
     */
    private String phone;

    /**
     * 用户id
     */
    @Column(name = "user_id")
    private String userId;

    /**
     * 退款金额
     */
    private Short amount;

    /**
     * 退款原因
     */
    private String reason;

    /**
     * 退款证明图片
     */
    private String url;

    /**
     * 状态：0 新退款订单，1退款中，

状态：0 新退款订单，1退款中，2退款成功，3退款失败
     */
    private Byte status;

    /**
     * 审核状态：0待审核，1已通过，2未通过
     */
    @Column(name = "audit_status")
    private Byte auditStatus;

    /**
     * 备注
     */
    private String remark;

    /**
     * 退款单号	
     */
    @Column(name = "refund_num")
    private String refundNum;

    @Column(name = "create_time")
    private Date createTime;

    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 退款时间
     */
    @Column(name = "refund_time")
    private Date refundTime;

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
     * 获取订单id
     *
     * @return order_id - 订单id
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置订单id
     *
     * @param orderId 订单id
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取手机号
     *
     * @return phone - 手机号
     */
    public String getPhone() {
        return phone;
    }

    /**
     * 设置手机号
     *
     * @param phone 手机号
     */
    public void setPhone(String phone) {
        this.phone = phone;
    }

    /**
     * 获取用户id
     *
     * @return user_id - 用户id
     */
    public String getUserId() {
        return userId;
    }

    /**
     * 设置用户id
     *
     * @param userId 用户id
     */
    public void setUserId(String userId) {
        this.userId = userId;
    }

    /**
     * 获取退款金额
     *
     * @return amount - 退款金额
     */
    public Short getAmount() {
        return amount;
    }

    /**
     * 设置退款金额
     *
     * @param amount 退款金额
     */
    public void setAmount(Short amount) {
        this.amount = amount;
    }

    /**
     * 获取退款原因
     *
     * @return reason - 退款原因
     */
    public String getReason() {
        return reason;
    }

    /**
     * 设置退款原因
     *
     * @param reason 退款原因
     */
    public void setReason(String reason) {
        this.reason = reason;
    }

    /**
     * 获取退款证明图片
     *
     * @return url - 退款证明图片
     */
    public String getUrl() {
        return url;
    }

    /**
     * 设置退款证明图片
     *
     * @param url 退款证明图片
     */
    public void setUrl(String url) {
        this.url = url;
    }

    /**
     * 获取状态：0 新退款订单，1退款中，

状态：0 新退款订单，1退款中，2退款成功，3退款失败
     *
     * @return status - 状态：0 新退款订单，1退款中，

状态：0 新退款订单，1退款中，2退款成功，3退款失败
     */
    public Byte getStatus() {
        return status;
    }

    /**
     * 设置状态：0 新退款订单，1退款中，

状态：0 新退款订单，1退款中，2退款成功，3退款失败
     *
     * @param status 状态：0 新退款订单，1退款中，

状态：0 新退款订单，1退款中，2退款成功，3退款失败
     */
    public void setStatus(Byte status) {
        this.status = status;
    }

    /**
     * 获取审核状态：0待审核，1已通过，2未通过
     *
     * @return audit_status - 审核状态：0待审核，1已通过，2未通过
     */
    public Byte getAuditStatus() {
        return auditStatus;
    }

    /**
     * 设置审核状态：0待审核，1已通过，2未通过
     *
     * @param auditStatus 审核状态：0待审核，1已通过，2未通过
     */
    public void setAuditStatus(Byte auditStatus) {
        this.auditStatus = auditStatus;
    }

    /**
     * 获取备注
     *
     * @return remark - 备注
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注
     *
     * @param remark 备注
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取退款单号	
     *
     * @return refund_num - 退款单号	
     */
    public String getRefundNum() {
        return refundNum;
    }

    /**
     * 设置退款单号	
     *
     * @param refundNum 退款单号	
     */
    public void setRefundNum(String refundNum) {
        this.refundNum = refundNum;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    /**
     * @return update_time
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * @param updateTime
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    /**
     * 获取退款时间
     *
     * @return refund_time - 退款时间
     */
    public Date getRefundTime() {
        return refundTime;
    }

    /**
     * 设置退款时间
     *
     * @param refundTime 退款时间
     */
    public void setRefundTime(Date refundTime) {
        this.refundTime = refundTime;
    }
}