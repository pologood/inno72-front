package com.inno72.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "inno72_order_history")
public class Inno72OrderHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "order_num")
    private String orderNum;

    @Column(name = "history_order")
    private String historyOrder;

    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 订单变更详情
     */
    private String details;

	public Inno72OrderHistory() {
		this.createTime = LocalDateTime.now();
	}

	public Inno72OrderHistory(String orderId, String orderNum, String historyOrder, String details) {
		this.orderId = orderId;
		this.orderNum = orderNum;
		this.historyOrder = historyOrder;
		this.details = details;
		this.createTime = LocalDateTime.now();
	}

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

    /**
     * @return order_num
     */
    public String getOrderNum() {
        return orderNum;
    }

    /**
     * @param orderNum
     */
    public void setOrderNum(String orderNum) {
        this.orderNum = orderNum;
    }

    /**
     * @return history_order
     */
    public String getHistoryOrder() {
        return historyOrder;
    }

    /**
     * @param historyOrder
     */
    public void setHistoryOrder(String historyOrder) {
        this.historyOrder = historyOrder;
    }

    /**
     * 获取订单变更详情
     *
     * @return details - 订单变更详情
     */
    public String getDetails() {
        return details;
    }

    /**
     * 设置订单变更详情
     *
     * @param details 订单变更详情
     */
    public void setDetails(String details) {
        this.details = details;
    }

	public LocalDateTime getCreateTime() {
		return createTime;
	}

	public void setCreateTime(LocalDateTime createTime) {
		this.createTime = createTime;
	}
}