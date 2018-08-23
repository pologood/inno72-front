package com.inno72.machine.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

public class Inno72SupplyChannelHistory {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    @Column(name="supply_channel_id")
    private String supplyChannelId;

    @Column(name="before_count")
    private int beforeCount;

    @Column(name="after_count")
    private int afterCount;

    @Column(name="batch_no")
    private String batchNo;

    @Column(name="machine_id")
    private String machineId;

    @Column(name = "user_id")
    private String userId;

    @Column(name="type")
    private int type;

    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name = "create_time")
    private LocalDateTime createTime;
    @Transient
    private String machineCode;

    @Transient
    private String localeStr;

    @Transient
    private int subCount;

    @Transient
    private String goodsName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getSupplyChannelId() {
        return supplyChannelId;
    }

    public void setSupplyChannelId(String supplyChannelId) {
        this.supplyChannelId = supplyChannelId;
    }

    public int getBeforeCount() {
        return beforeCount;
    }

    public void setBeforeCount(int beforeCount) {
        this.beforeCount = beforeCount;
    }

    public int getAfterCount() {
        return afterCount;
    }

    public void setAfterCount(int afterCount) {
        this.afterCount = afterCount;
    }

    public String getBatchNo() {
        return batchNo;
    }

    public void setBatchNo(String batchNo) {
        this.batchNo = batchNo;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getSubCount() {
        return subCount;
    }

    public void setSubCount(int subCount) {
        this.subCount = subCount;
    }

    public String getGoodsName() {
        return goodsName;
    }

    public void setGoodsName(String goodsName) {
        this.goodsName = goodsName;
    }
}
