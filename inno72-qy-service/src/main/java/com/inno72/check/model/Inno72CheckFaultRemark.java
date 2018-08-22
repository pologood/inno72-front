package com.inno72.check.model;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.datetime.CustomLocalDateTimeSerializer;
import org.springframework.data.jpa.convert.threeten.Jsr310JpaConverters;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

public class Inno72CheckFaultRemark {

    /**
     * uuid
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 故障ID
     */
    @Column(name="fault_id")
    private String faultId;

    /**
     * 用户ID
     */
    @Column(name="user_id")
    private String userId;

    /**
     * 用户姓名
     */
    @Column(name="name")
    private String name;

    /**
     * 用户类型
     */
    @Column(name="type")
    private int type;

    /**
     * 备注
     */
    @Column(name="remark")
    private String remark;

    /**
     * 提交时间
     */
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @Convert(converter = Jsr310JpaConverters.LocalDateTimeConverter.class)
    @Column(name="create_time")
    private LocalDateTime createTime;

    /**
     * 是否删除
     */
    @Column(name="is_delete")
    private int isDelete;

    @Transient
    private List<Inno72CheckFaultImage> imageList;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFaultId() {
        return faultId;
    }

    public void setFaultId(String faultId) {
        this.faultId = faultId;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public LocalDateTime getCreateTime() {
        return createTime;
    }

    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    public int getIsDelete() {
        return isDelete;
    }

    public void setIsDelete(int isDelete) {
        this.isDelete = isDelete;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Inno72CheckFaultImage> getImageList() {
        return imageList;
    }

    public void setImageList(List<Inno72CheckFaultImage> imageList) {
        this.imageList = imageList;
    }
}
