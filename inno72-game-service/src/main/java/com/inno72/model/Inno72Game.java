package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;
import com.inno72.common.LocalDateConverter;

@Table(name = "inno72_game")
public class Inno72Game {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private String id;

	/**
	 * 游戏名称
	 */
	private String name;

	/**
	 * 游戏版本
	 */
	private String version;

	/**
	 * 游戏版本（点72）
	 */
	@Column(name = "version_inno72")
	private String versionInno72;

	/**
	 * 游戏描述
	 */
	private String remark;

	/**
	 * 是否删除：0未删除，1已删除
	 */
	@Column(name = "is_delete")
	private Integer isDelete;

	/**
	 * 创建人
	 */
	@Column(name = "create_id")
	private String createId;
	/**
	 * 商品最小数量
	 */
	@Column(name = "min_goods_num")
	private Integer minGoodsNum;

	/**
	 * 商品最大数量
	 */
	@Column(name = "max_goods_num")
	private Integer maxGoodsNum;

	/**
	 * 创建时间
	 */
	@Column(name = "create_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime createTime;

	/**
	 * 更新人
	 */
	@Column(name = "update_id")
	private String updateId;

	/**
	 * 更新时间
	 */
	@Column(name = "update_time")
	@JsonSerialize(using = CustomLocalDateTimeSerializer.class)
	@Convert(converter = LocalDateConverter.class)
	private LocalDateTime updateTime;

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
	 * 获取游戏名称
	 *
	 * @return name - 游戏名称
	 */
	public String getName() {
		return name;
	}

	/**
	 * 设置游戏名称
	 *
	 * @param name 游戏名称
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 获取游戏版本
	 *
	 * @return version - 游戏版本
	 */
	public String getVersion() {
		return version;
	}

	/**
	 * 设置游戏版本
	 *
	 * @param version 游戏版本
	 */
	public void setVersion(String version) {
		this.version = version;
	}

	/**
	 * 获取游戏版本（点72）
	 *
	 * @return version_inno72 - 游戏版本（点72）
	 */
	public String getVersionInno72() {
		return versionInno72;
	}

	/**
	 * 设置游戏版本（点72）
	 *
	 * @param versionInno72 游戏版本（点72）
	 */
	public void setVersionInno72(String versionInno72) {
		this.versionInno72 = versionInno72;
	}

	/**
	 * 获取游戏描述
	 *
	 * @return remark - 游戏描述
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * 设置游戏描述
	 *
	 * @param remark 游戏描述
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * 获取是否删除：0未删除，1已删除
	 *
	 * @return is_delete - 是否删除：0未删除，1已删除
	 */
	public Integer getIsDelete() {
		return isDelete;
	}

	/**
	 * 设置是否删除：0未删除，1已删除
	 *
	 * @param isDelete 是否删除：0未删除，1已删除
	 */
	public void setIsDelete(Integer isDelete) {
		this.isDelete = isDelete;
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

	public Integer getMinGoodsNum() {
		return minGoodsNum;
	}

	public void setMinGoodsNum(Integer minGoodsNum) {
		this.minGoodsNum = minGoodsNum;
	}

	public Integer getMaxGoodsNum() {
		return maxGoodsNum;
	}

	public void setMaxGoodsNum(Integer maxGoodsNum) {
		this.maxGoodsNum = maxGoodsNum;
	}
}