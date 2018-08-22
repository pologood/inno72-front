package com.inno72.check.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72CheckFault {

    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 机器ID
     */
    @Column(name="machine_id")
    private String machineId;

    /**
     * 工单号
     */
    @Column(name="code")
    private String code;

    /**
     * title
     */
    @Column(name="title")
    private String title;

    /**
     * 工单类型（1.故障，2.报警，3.补货，4.投诉）
     */
    @Column(name="work_type")
    private Integer workType;

    /**
     * 工单状态（1.待接单，2.待处理，3.已完成，4.已确认，5.已关闭）
     */
    @Column(name="status")
    private Integer status;

    /**
     * 来源(1.巡检上报，2.运营派单，3.报警派单)
     */
    @Column(name="source")
    private Integer source;

    /**
     * 紧急状态（1.日常，2.紧急）
     */
    @Column(name="urgent_status")
    private Integer urgentStatus;
    /**
     * 故障类型
     */
    @Column(name="type")
    private String type;


    /**
     * 工单描述
     */
    @Column(name="remark")
    private String remark;

    /**
     * 派单人
     */
    @Column(name="submit_user")
    private String submitUser;

    /**
     * 派单人ID
     */
    @Column(name="submit_id")
    private String submitId;

    /**
     * 派单人类型（1.巡检人员，2.运营人员）
     */
    @Column(name="submit_user_type")
    private Integer submitUserType;

    /**
     * 接单人
     */
    @Column(name="receive_user")
    private String receiveUser;

    /**
     * 接单人ID
     */
    @Column(name="receive_id")
    private String receiveId;

    /**
     * 解决人
     */
    @Column(name="finish_user")
    private String finishUser;

    /**
     * 解决人ID
     */
    @Column(name="finish_id")
    private String finishId;

    /**
     * 提交时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="submit_time")
    private LocalDateTime submitTime;

    /**
     *接单时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="talking_time")
    private LocalDateTime talkingTime;

    /**
     * 解决时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="finish_time")
    private LocalDateTime finishTime;

    /**
     * 修改时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="update_time")
    private LocalDateTime updateTime;

    /**
     * 解决方案
     */
    @Column(name="finish_remark")
    private String finishRemark;

    /**
     * 提醒状态（0.未提醒，1.已提醒）
     */
    @Column(name="remind_status")
    private Integer remindStatus;

    @Transient
    private String machineCode;

    @Transient
    private List<Inno72CheckFaultRemark> remarkList;

    @Transient
    private String[] images;

    @Transient
    private String[] machineIds;


    @Transient
    private String typeName;

    @Transient
    private Integer finishShow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getWorkType() {
        return workType;
    }

    public void setWorkType(Integer workType) {
        this.workType = workType;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getSource() {
        return source;
    }

    public void setSource(Integer source) {
        this.source = source;
    }

    public Integer getUrgentStatus() {
        return urgentStatus;
    }

    public void setUrgentStatus(Integer urgentStatus) {
        this.urgentStatus = urgentStatus;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getSubmitUser() {
        return submitUser;
    }

    public void setSubmitUser(String submitUser) {
        this.submitUser = submitUser;
    }

    public String getSubmitId() {
        return submitId;
    }

    public void setSubmitId(String submitId) {
        this.submitId = submitId;
    }

    public Integer getSubmitUserType() {
        return submitUserType;
    }

    public void setSubmitUserType(Integer submitUserType) {
        this.submitUserType = submitUserType;
    }

    public String getReceiveUser() {
        return receiveUser;
    }

    public void setReceiveUser(String receiveUser) {
        this.receiveUser = receiveUser;
    }

    public String getReceiveId() {
        return receiveId;
    }

    public void setReceiveId(String receiveId) {
        this.receiveId = receiveId;
    }

    public String getFinishUser() {
        return finishUser;
    }

    public void setFinishUser(String finishUser) {
        this.finishUser = finishUser;
    }

    public String getFinishId() {
        return finishId;
    }

    public void setFinishId(String finishId) {
        this.finishId = finishId;
    }

    public LocalDateTime getSubmitTime() {
        return submitTime;
    }

    public void setSubmitTime(LocalDateTime submitTime) {
        this.submitTime = submitTime;
    }

    public LocalDateTime getTalkingTime() {
        return talkingTime;
    }

    public void setTalkingTime(LocalDateTime talkingTime) {
        this.talkingTime = talkingTime;
    }

    public LocalDateTime getFinishTime() {
        return finishTime;
    }

    public void setFinishTime(LocalDateTime finishTime) {
        this.finishTime = finishTime;
    }

    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

    public String getFinishRemark() {
        return finishRemark;
    }

    public void setFinishRemark(String finishRemark) {
        this.finishRemark = finishRemark;
    }

    public Integer getRemindStatus() {
        return remindStatus;
    }

    public void setRemindStatus(Integer remindStatus) {
        this.remindStatus = remindStatus;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public List<Inno72CheckFaultRemark> getRemarkList() {
        return remarkList;
    }

    public void setRemarkList(List<Inno72CheckFaultRemark> remarkList) {
        this.remarkList = remarkList;
    }

    public String[] getImages() {
        return images;
    }

    public void setImages(String[] images) {
        this.images = images;
    }

    public String[] getMachineIds() {
        return machineIds;
    }

    public void setMachineIds(String[] machineIds) {
        this.machineIds = machineIds;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    public Integer getFinishShow() {
        return finishShow;
    }

    public void setFinishShow(Integer finishShow) {
        this.finishShow = finishShow;
    }
}
