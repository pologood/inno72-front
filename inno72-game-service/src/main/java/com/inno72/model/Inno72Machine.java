package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_machine")
public class Inno72Machine {
    @Id
    @Column(name = "Id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 机器id
     */
    @Column(name = "machine_id")
    private String machineId;

    /**
     * 机器名称
     */
    @Column(name = "machine_name")
    private String machineName;

    /**
     * 所属点位
     */
    @Column(name = "locale_id")
    private String localeId;

    /**
     * 机器所属标签
     */
    private String tag;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return Id
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
     * 获取机器id
     *
     * @return machine_id - 机器id
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置机器id
     *
     * @param machineId 机器id
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取机器名称
     *
     * @return machine_name - 机器名称
     */
    public String getMachineName() {
        return machineName;
    }

    /**
     * 设置机器名称
     *
     * @param machineName 机器名称
     */
    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    /**
     * 获取所属点位
     *
     * @return locale_id - 所属点位
     */
    public String getLocaleId() {
        return localeId;
    }

    /**
     * 设置所属点位
     *
     * @param localeId 所属点位
     */
    public void setLocaleId(String localeId) {
        this.localeId = localeId;
    }

    /**
     * 获取机器所属标签
     *
     * @return tag - 机器所属标签
     */
    public String getTag() {
        return tag;
    }

    /**
     * 设置机器所属标签
     *
     * @param tag 机器所属标签
     */
    public void setTag(String tag) {
        this.tag = tag;
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