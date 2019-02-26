package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;

@Table(name = "inno72_store")
public class Inno72Store {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 仓库名称
     */
    private String name;

    /**
     * 仓库类型:0 中心仓，1分仓
     */
    private String type;

    /**
     * 区域ID
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 详细地址
     */
    private String area;

    /**
     * 容量（方）
     */
    private Integer capacity;

    /**
     * 使用开始时间
     */
    @Column(name = "start_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime startTime;

    /**
     * 使用结束时间
     */
    @Column(name = "end_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime endTime;

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
    @Column(name = "create_id")
    private String createId;

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
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
	@JsonFormat(pattern="yyyy-MM-dd HH:mm:ss")
	@DateTimeFormat(pattern="yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

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
     * 获取仓库名称
     *
     * @return name - 仓库名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置仓库名称
     *
     * @param name 仓库名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取仓库类型:0 中心仓，1分仓
     *
     * @return type - 仓库类型:0 中心仓，1分仓
     */
    public String getType() {
        return type;
    }

    /**
     * 设置仓库类型:0 中心仓，1分仓
     *
     * @param type 仓库类型:0 中心仓，1分仓
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 获取区域ID
     *
     * @return area_code - 区域ID
     */
    public String getAreaCode() {
        return areaCode;
    }

    /**
     * 设置区域ID
     *
     * @param areaCode 区域ID
     */
    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    /**
     * 获取详细地址
     *
     * @return area - 详细地址
     */
    public String getArea() {
        return area;
    }

    /**
     * 设置详细地址
     *
     * @param area 详细地址
     */
    public void setArea(String area) {
        this.area = area;
    }

    /**
     * 获取容量（方）
     *
     * @return capacity - 容量（方）
     */
    public Integer getCapacity() {
        return capacity;
    }

    /**
     * 设置容量（方）
     *
     * @param capacity 容量（方）
     */
    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    /**
     * 获取使用开始时间
     *
     * @return start_time - 使用开始时间
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置使用开始时间
     *
     * @param startTime 使用开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取使用结束时间
     *
     * @return end_time - 使用结束时间
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置使用结束时间
     *
     * @param endTime 使用结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
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
}