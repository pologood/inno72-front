package com.inno72.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inno72_feedback_errorlog")
public class Inno72FeedbackErrorlog {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 淘宝订单号
     */
    @Column(name = "order_id")
    private String orderId;

    @Column(name = "create_time")
    private Date createTime;

    private String msg;

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
     * 获取淘宝订单号
     *
     * @return order_id - 淘宝订单号
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置淘宝订单号
     *
     * @param orderId 淘宝订单号
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
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
     * @return msg
     */
    public String getMsg() {
        return msg;
    }

    /**
     * @param msg
     */
    public void setMsg(String msg) {
        this.msg = msg;
    }
}