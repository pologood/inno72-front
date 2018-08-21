package com.inno72.vo;

import java.math.BigDecimal;
import java.util.Date;

/*
 * 派样商品表
 */
public class Inno72SamplingGoods {
	/**
	 * 商品ID
	 */
	private String id;

	/**
	 * 商品名称
	 */
	private String name;

	/**
	 * 商品编码
	 */
	private String code;

	/**
	 * 商品价格
	 */
	private BigDecimal price;

	/**
	 * 商户ID
	 */
	private String sellerId;

	/**
	 * 店铺ID
	 */
	private String shopId;

	/**
	 * 图片
	 */
	private String img;

	/**
	 * 状态：0正常，1下架
	 */
	private Integer isDelete;

	/**
	 * 备注描述
	 */
	private String remark;

	/**
	 * 创建人
	 */
	private String createId;

	/**
	 * 创建时间
	 */
	private Date createTime;

	/**
	 * 更新人
	 */
	private String updateId;

	/**
	 * 更新时间
	 */
	private Date updateTime;

	/**
	 * 商品数量
	 */
	private Integer num;

	/**
	 * 获取商品ID
	 *
	 * @return id - 商品ID
	 */
	public String getId() {
		return id;
	}

	/**
	 * 设置商品ID
	 *
	 * @param id 商品ID
	 */
	public void setId(String id) {
		this.id = id;
	}

	/**
	 * 获取商品名称
	 *
	 * @return name - 商品名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置商品名称
	 *
	 * @param name 商品名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取商品编码
	 *
	 * @return code - 商品编码
	 */
	public String getCode() {
		return code;
	}

	/**
	 * 设置商品编码
	 *
	 * @param code 商品编码
	 */
	public void setCode(String code) {
		this.code = code;
	}

	/**
	 * 获取商品价格
	 *
	 * @return price - 商品价格
	 */
	public BigDecimal getPrice() {
		return price;
	}

	/**
	 * 设置商品价格
	 *
	 * @param price 商品价格
	 */
	public void setPrice(BigDecimal price) {
		this.price = price;
	}

	/**
	 * 获取商户ID
	 *
	 * @return seller_id - 商户ID
	 */
	public String getSellerId() {
		return sellerId;
	}

	/**
	 * 设置商户ID
	 *
	 * @param sellerId 商户ID
	 */
	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
	}

	/**
	 * 获取图片
	 *
	 * @return img - 图片
	 */
	public String getImg() {
		return img;
	}

	/**
	 * 设置图片
	 *
	 * @param img 图片
	 */
	public void setImg(String img) {
		this.img = img;
	}

	/**
	 * 获取状态：0正常，1下架
	 *
	 * @return is_delete - 状态：0正常，1下架
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置状态：0正常，1下架
	 *
	 * @param isDelete 状态：0正常，1下架
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
	 * @return create_id - 创建人
	 */
	public String getCreateId() {
		return createId;
	}

	/**
	 * 设置创建人
	 *
	 * @param createId 创建人
	 */
	public void setCreateId(String createId) {
		this.createId = createId;
	}

	/**
	 * 获取创建时间
	 *
	 * @return create_time - 创建时间
	 */
	public Date getCreateTime() {
		return createTime;
	}

	/**
	 * 设置创建时间
	 *
	 * @param createTime 创建时间
	 */
	public void setCreateTime(Date createTime) {
		this.createTime = createTime;
	}

	/**
	 * 获取更新人
	 *
	 * @return update_id - 更新人
	 */
	public String getUpdateId() {
		return updateId;
	}

	/**
	 * 设置更新人
	 *
	 * @param updateId 更新人
	 */
	public void setUpdateId(String updateId) {
		this.updateId = updateId;
	}

	/**
	 * 获取更新时间
	 *
	 * @return update_time - 更新时间
	 */
	public Date getUpdateTime() {
		return updateTime;
	}

	/**
	 * 设置更新时间
	 *
	 * @param updateTime 更新时间
	 */
	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}

	public Integer getNum() {
		return num;
	}

	public void setNum(Integer num) {
		this.num = num;
	}

	public String getShopId() {
		return shopId;
	}

	public void setShopId(String shopId) {
		this.shopId = shopId;
	}

}