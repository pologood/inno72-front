package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_locale")
public class Inno72Locale {
    /**
     * 点位ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 点位名称
     */
    private String name;

    /**
     * 区域ID
     */
    @Column(name = "area_code")
    private String areaCode;

    /**
     * 商场
     */
    private String mall;

    /**
     * 运营人员
     */
    private String manager;

    /**
     * 运营人员手机
     */
    private String mobile;

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
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * 获取点位ID
     *
     * @return id - 点位ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置点位ID
     *
     * @param id 点位ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取点位名称
     *
     * @return name - 点位名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置点位名称
     *
     * @param name 点位名称
     */
    public void setName(String name) {
        this.name = name;
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
     * 获取商场
     *
     * @return mall - 商场
     */
    public String getMall() {
        return mall;
    }

    /**
     * 设置商场
     *
     * @param mall 商场
     */
    public void setMall(String mall) {
        this.mall = mall;
    }

    /**
     * 获取运营人员
     *
     * @return manager - 运营人员
     */
    public String getManager() {
        return manager;
    }

    /**
     * 设置运营人员
     *
     * @param manager 运营人员
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * 获取运营人员手机
     *
     * @return mobile - 运营人员手机
     */
    public String getMobile() {
        return mobile;
    }

    /**
     * 设置运营人员手机
     *
     * @param mobile 运营人员手机
     */
    public void setMobile(String mobile) {
        this.mobile = mobile;
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
}