package com.inno72.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_supply_channel")
public class Inno72SupplyChannel implements Serializable {
    /**
     *
     */
    private static final long serialVersionUID = 889207530103014551L;

    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 货道编号
     */
    private String code;

    /**
     * 货道名称
     */
    private String name;

    /**
     * 状态（0.未合并，1.已合并）
     */
    private Integer status;

    /**
     * 机器编号
     */
    @Column(name = "machine_id")
    private String machineId;

    /**
     * 父货道编号
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 商品容量
     */
    @Column(name = "volume_count")
    private Integer volumeCount;

    /**
     * 商品数量
     */
    @Column(name = "goods_count")
    private Integer goodsCount;


    /**
     * 创建人ID
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 修改人ID
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 修改时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Column(name = "update_time")
    private Date updateTime;

    @Transient
    private String goodsName;

    @Transient
    private String[] goodsCodes;

    @Transient
    private String goodsCode;

    public Inno72SupplyChannel() {
    }

    public Inno72SupplyChannel(String machineId, String[] goodsCodes, String code) {
        this.machineId = machineId;
        this.goodsCodes = goodsCodes;
        this.goodsCode = code;
    }

    /**
     * 获取UUID
     *
     * @RETURN ID - UUID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置uuid
     *
     * @param id uuid
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取货道编号
     *
     * @return code - 货道编号
     */
    public String getCode() {
        return code;
    }

    /**
     * 设置货道编号
     *
     * @param code 货道编号
     */
    public void setCode(String code) {
        this.code = code;
    }

    /**
     * 获取货道名称
     *
     * @return name - 货道名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置货道名称
     *
     * @param name 货道名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取状态（0.未合并，1.已合并）
     *
     * @return status - 状态（0.未合并，1.已合并）
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态（0.未合并，1.已合并）
     *
     * @param status 状态（0.未合并，1.已合并）
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取机器编号
     *
     * @return merchant_id - 机器编号
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置机器编号
     *
     * @param merchantId 机器编号
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * 获取父货道编号
     *
     * @return parent_code - 父货道编号
     */
    public String getParentCode() {
        return parentCode;
    }

    /**
     * 设置父货道编号
     *
     * @param parentCode 父货道编号
     */
    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    /**
     * 获取商品容量
     *
     * @return volume_count - 商品容量
     */
    public Integer getVolumeCount() {
        return volumeCount;
    }

    /**
     * 设置商品容量
     *
     * @param volumeCount 商品容量
     */
    public void setVolumeCount(Integer volumeCount) {
        this.volumeCount = volumeCount;
    }

    /**
     * 获取商品数量
     *
     * @return goods_count - 商品数量
     */
    public Integer getGoodsCount() {
        return goodsCount;
    }

    /**
     * 设置商品数量
     *
     * @param goodsCount 商品数量
     */
    public void setGoodsCount(Integer goodsCount) {
        this.goodsCount = goodsCount;
    }


    /**
     * 获取创建人ID
     *
     * @return create_id - 创建人ID
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人ID
     *
     * @param createId 创建人ID
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
     * 获取修改人ID
     *
     * @return update_id - 修改人ID
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置修改人ID
     *
     * @param updateId 修改人ID
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取修改时间
     *
     * @return update_time - 修改时间
     */
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置修改时间
     *
     * @param updateTime 修改时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public String[] getGoodsCodes() {
        return goodsCodes;
    }

    public void setGoodsCodes(String[] goodsCodes) {
        this.goodsCodes = goodsCodes;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }

    public String getGoodsCode() {
        return goodsCode;
    }

    public void setGoodsCode(String goodsCode) {
        this.goodsCode = goodsCode;
    }
}