package com.inno72.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.inno72.common.util.UuidUtil;

@Table(name = "inno72_store_order")
public class Inno72StoreOrder {
	/**
	 * ID
	 */
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 编号 JC20190103100129
	 */
	@Column(name = "order_num")
	private String orderNum;

	/**
	 * 库单类型：0入库单，1出库单
	 */
	@Column(name = "order_type")
	private Integer orderType;

	public static enum Inno72StoreOrder_OrderType {

		IN("0", "入库单"), OUT("1", "出库单"),;

		private String code;
		private String desc;

		Inno72StoreOrder_OrderType(String code, String desc) {
			this.code = code;
			this.desc = desc;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getDesc() {
			return desc;
		}

		public void setDesc(String desc) {
			this.desc = desc;
		}
	}

	/**
	 * 商品 ID
	 */
	private String goods;

	/**
	 * 活动 ID
	 */
	private String activity;

	/**
	 * 商家 sellerId
	 */
	private String merchant;

	/**
	 * 发货方 商户名称
	 */
	private String sender;

	/**
	 * 发货方ID 商户ID
	 */
	@Column(name = "send_id")
	private String sendId;

	/**
	 * 发货方类型 0：商家； 1：巡检发货； 2：仓库发货
	 */
	@Column(name = "send_type")
	private Integer sendType;

	/**
	 * 收货方 仓库名称
	 */
	private String receiver;

	/**
	 * 收货方ID 仓库ID
	 */
	@Column(name = "receive_id")
	private String receiveId;

	/**
	 * 收货方类型 0：商家； 1：巡检发货； 2：仓库收货
	 */
	@Column(name = "receive_type")
	private Integer receiveType;

	/**
	 * 出库/应入库商品数量 填写
	 */
	private Integer number;

	/**
	 * 出库所占容量（方）
	 */
	private Integer capacity;

	/**
	 * 实收商品数量
	 */
	@Column(name = "receive_number")
	private Integer receiveNumber;

	/**
	 * 实收所占容量（方）
	 */
	@Column(name = "receive_capacity")
	private Integer receiveCapacity;

	/**
	 * 收货时间（最后物流单签收时间）
	 */
	@Column(name = "receive_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime receiveTime;

	/**
	 * 入库状态：0待入库，1已完成，2已废弃
	 */
	private Integer status;

	/**
	 * 状态：0正常，1删除
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
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	private String updater;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
	private LocalDateTime updateTime;

	public Inno72StoreOrder() {
	}


	public Inno72StoreOrder(String activity, String goods, String merchant, String sender, String sendId, String receiver,
			String receiveId, Integer number, String creater) {

		this.activity = activity;
		this.goods = goods;
		this.merchant = merchant;
		this.sender = sender;
		this.sendId = sendId;
		this.receiver = receiver;
		this.receiveId = receiveId;
		this.number = number;
		this.creater = creater;
		this.updater = creater;

		LocalDateTime now = LocalDateTime.now();
		this.id = UuidUtil.getUUID32();
		this.orderNum = "JC" + now.format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"))
				+ ((int) (Math.random() * 10000) + "");
		this.orderType = 0;// 入库单
		this.sendType = 0;// 发货方类型 0：商家
		this.receiveType = 2;// 收货方类型 2：仓库
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
	 * 获取编号
	 *
	 * @return order_num - 编号
	 */
	public String getOrderNum() {
		return orderNum;
	}

	/**
	 * 设置编号
	 *
	 * @param orderNum 编号
	 */
	public void setOrderNum(String orderNum) {
		this.orderNum = orderNum;
	}

	/**
	 * 获取库单类型：0入库单，1出库单
	 *
	 * @return order_type - 库单类型：0入库单，1出库单
	 */
	public Integer getOrderType() {
		return orderType;
	}

	/**
	 * 设置库单类型：0入库单，1出库单
	 *
	 * @param orderType 库单类型：0入库单，1出库单
	 */
	public void setOrderType(Integer orderType) {
		this.orderType = orderType;
	}

	/**
	 * 获取商品
	 *
	 * @return goods - 商品
	 */
	public String getGoods() {
		return goods;
	}

	/**
	 * 设置商品
	 *
	 * @param goods 商品
	 */
	public void setGoods(String goods) {
		this.goods = goods;
	}

	/**
	 * 获取商家
	 *
	 * @return merchant - 商家
	 */
	public String getMerchant() {
		return merchant;
	}

	/**
	 * 设置商家
	 *
	 * @param merchant 商家
	 */
	public void setMerchant(String merchant) {
		this.merchant = merchant;
	}

	/**
	 * 获取发货方
	 *
	 * @return sender - 发货方
	 */
	public String getSender() {
		return sender;
	}

	/**
	 * 设置发货方
	 *
	 * @param sender 发货方
	 */
	public void setSender(String sender) {
		this.sender = sender;
	}

	/**
	 * 获取发货方ID
	 *
	 * @return send_id - 发货方ID
	 */
	public String getSendId() {
		return sendId;
	}

	/**
	 * 设置发货方ID
	 *
	 * @param sendId 发货方ID
	 */
	public void setSendId(String sendId) {
		this.sendId = sendId;
	}

	/**
	 * 获取发货方类型
	 *
	 * @return send_type - 发货方类型
	 */
	public Integer getSendType() {
		return sendType;
	}

	/**
	 * 设置发货方类型
	 *
	 * @param sendType 发货方类型
	 */
	public void setSendType(Integer sendType) {
		this.sendType = sendType;
	}

	/**
	 * 获取收货方
	 *
	 * @return receiver - 收货方
	 */
	public String getReceiver() {
		return receiver;
	}

	/**
	 * 设置收货方
	 *
	 * @param receiver 收货方
	 */
	public void setReceiver(String receiver) {
		this.receiver = receiver;
	}

	/**
	 * 获取收货方ID
	 *
	 * @return receive_id - 收货方ID
	 */
	public String getReceiveId() {
		return receiveId;
	}

	/**
	 * 设置收货方ID
	 *
	 * @param receiveId 收货方ID
	 */
	public void setReceiveId(String receiveId) {
		this.receiveId = receiveId;
	}

	/**
	 * 获取收货方类型
	 *
	 * @return receive_type - 收货方类型
	 */
	public Integer getReceiveType() {
		return receiveType;
	}

	/**
	 * 设置收货方类型
	 *
	 * @param receiveType 收货方类型
	 */
	public void setReceiveType(Integer receiveType) {
		this.receiveType = receiveType;
	}

	/**
	 * 获取出库/应入库商品数量
	 *
	 * @return number - 出库/应入库商品数量
	 */
	public Integer getNumber() {
		return number;
	}

	/**
	 * 设置出库/应入库商品数量
	 *
	 * @param number 出库/应入库商品数量
	 */
	public void setNumber(Integer number) {
		this.number = number;
	}

	/**
	 * 获取出库所占容量（方）
	 *
	 * @return capacity - 出库所占容量（方）
	 */
	public Integer getCapacity() {
		return capacity;
	}

	/**
	 * 设置出库所占容量（方）
	 *
	 * @param capacity 出库所占容量（方）
	 */
	public void setCapacity(Integer capacity) {
		this.capacity = capacity;
	}

	/**
	 * 获取实收商品数量
	 *
	 * @return receive_number - 实收商品数量
	 */
	public Integer getReceiveNumber() {
		return receiveNumber;
	}

	/**
	 * 设置实收商品数量
	 *
	 * @param receiveNumber 实收商品数量
	 */
	public void setReceiveNumber(Integer receiveNumber) {
		this.receiveNumber = receiveNumber;
	}

	/**
	 * 获取实收所占容量（方）
	 *
	 * @return receive_capacity - 实收所占容量（方）
	 */
	public Integer getReceiveCapacity() {
		return receiveCapacity;
	}

	/**
	 * 设置实收所占容量（方）
	 *
	 * @param receiveCapacity 实收所占容量（方）
	 */
	public void setReceiveCapacity(Integer receiveCapacity) {
		this.receiveCapacity = receiveCapacity;
	}

	/**
	 * 获取收货时间（最后物流单签收时间）
	 *
	 * @return receive_time - 收货时间（最后物流单签收时间）
	 */
	public LocalDateTime getReceiveTime() {
		return receiveTime;
	}

	/**
	 * 设置收货时间（最后物流单签收时间）
	 *
	 * @param receiveTime 收货时间（最后物流单签收时间）
	 */
	public void setReceiveTime(LocalDateTime receiveTime) {
		this.receiveTime = receiveTime;
	}

	/**
	 * 获取入库状态：0待入库，1已完成，2已废弃
	 *
	 * @return status - 入库状态：0待入库，1已完成，2已废弃
	 */
	public Integer getStatus() {
		return status;
	}

	/**
	 * 设置入库状态：0待入库，1已完成，2已废弃
	 *
	 * @param status 入库状态：0待入库，1已完成，2已废弃
	 */
	public void setStatus(Integer status) {
		this.status = status;
	}

	/**
	 * 获取状态：0正常，1删除
	 *
	 * @return is_delete - 状态：0正常，1删除
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置状态：0正常，1删除
	 *
	 * @param isDelete 状态：0正常，1删除
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

	public String getActivity() {
		return activity;
	}

	public void setActivity(String activity) {
		this.activity = activity;
	}
}

