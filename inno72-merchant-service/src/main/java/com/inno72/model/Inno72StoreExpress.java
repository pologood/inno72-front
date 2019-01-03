package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inno72.common.util.UuidUtil;

@Table(name = "inno72_store_express")
public class Inno72StoreExpress {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 出入库单ID
     */
    @Column(name = "order_id")
    private String orderId;

    /**
     * 物流单号
     */
    @Column(name = "express_num")
    private String expressNum;

    /**
     * 物流公司
     */
    @Column(name = "express_company")
    private String expressCompany;

    /**
     * 商品数量
     */
    private Integer number;

    /**
     * 签收收商品数量
     */
    @Column(name = "receive_number")
    private Integer receiveNumber;

    /**
     * 签收收所占容量（方）
     */
    @Column(name = "receive_capacity")
    private Integer receiveCapacity;

    /**
     * 签收时间
     */
    @Column(name = "receive_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime receiveTime;

    /**
     * 签收人
     */
    private String receiver;

    /**
     * 状态：0未签收，1签收 填写
     */
    private Integer status;

    /**
     * 状态：0正常，1停止
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 创建人
     */
    private String creater;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    private String updater;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

	public Inno72StoreExpress() {
	}

	/*
	 * @param orderId 出入库单ID
	 * @param expressNum 物流单号
	 * @param expressCompany 物流公司
	 * @param number 数量
	 * @param creater
	 */
	public Inno72StoreExpress(String orderId, String expressNum, String expressCompany, Integer number,
			String creater) {
		LocalDateTime now = LocalDateTime.now();
		this.id = UuidUtil.getUUID32();
		this.orderId = orderId;
		this.expressNum = expressNum;//单号
		this.expressCompany = expressCompany;//公司
		this.number = number;
		this.creater = creater;
		this.updater = creater;
		this.status = 0;
		this.isDelete = 0;
		this.createTime = now;
		this.updateTime = now;
	}

	/**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取出入库单ID
     *
     * @return order_id - 出入库单ID
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * 设置出入库单ID
     *
     * @param orderId 出入库单ID
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * 获取物流单号
     *
     * @return express_num - 物流单号
     */
    public String getExpressNum() {
        return expressNum;
    }

    /**
     * 设置物流单号
     *
     * @param expressNum 物流单号
     */
    public void setExpressNum(String expressNum) {
        this.expressNum = expressNum;
    }

    /**
     * 获取物流公司
     *
     * @return express_company - 物流公司
     */
    public String getExpressCompany() {
        return expressCompany;
    }

    /**
     * 设置物流公司
     *
     * @param expressCompany 物流公司
     */
    public void setExpressCompany(String expressCompany) {
        this.expressCompany = expressCompany;
    }

    /**
     * 获取商品数量
     *
     * @return number - 商品数量
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置商品数量
     *
     * @param number 商品数量
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取签收收商品数量
     *
     * @return receive_number - 签收收商品数量
     */
    public Integer getReceiveNumber() {
        return receiveNumber;
    }

    /**
     * 设置签收收商品数量
     *
     * @param receiveNumber 签收收商品数量
     */
    public void setReceiveNumber(Integer receiveNumber) {
        this.receiveNumber = receiveNumber;
    }

    /**
     * 获取签收收所占容量（方）
     *
     * @return receive_capacity - 签收收所占容量（方）
     */
    public Integer getReceiveCapacity() {
        return receiveCapacity;
    }

    /**
     * 设置签收收所占容量（方）
     *
     * @param receiveCapacity 签收收所占容量（方）
     */
    public void setReceiveCapacity(Integer receiveCapacity) {
        this.receiveCapacity = receiveCapacity;
    }

    /**
     * 获取签收时间
     *
     * @return receive_time - 签收时间
     */
    public LocalDateTime getReceiveTime() {
        return receiveTime;
    }

    /**
     * 设置签收时间
     *
     * @param receiveTime 签收时间
     */
    public void setReceiveTime(LocalDateTime receiveTime) {
        this.receiveTime = receiveTime;
    }

    /**
     * 获取签收人
     *
     * @return receiver - 签收人
     */
    public String getReceiver() {
        return receiver;
    }

    /**
     * 设置签收人
     *
     * @param receiver 签收人
     */
    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    /**
     * 获取状态：0未签收，1签收
     *
     * @return status - 状态：0未签收，1签收
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0未签收，1签收
     *
     * @param status 状态：0未签收，1签收
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取状态：0正常，1停止
     *
     * @return is_delete - 状态：0正常，1停止
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置状态：0正常，1停止
     *
     * @param isDelete 状态：0正常，1停止
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取备注描述
     *
     * @return remark - 备注描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注描述
     *
     * @param remark 备注描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return creater - 创建人
     */
    public String getCreater() {
        return creater;
    }

    /**
     * 设置创建人
     *
     * @param creater 创建人
     */
    public void setCreater(String creater) {
        this.creater = creater;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人
     *
     * @return updater - 更新人
     */
    public String getUpdater() {
        return updater;
    }

    /**
     * 设置更新人
     *
     * @param updater 更新人
     */
    public void setUpdater(String updater) {
        this.updater = updater;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }
}