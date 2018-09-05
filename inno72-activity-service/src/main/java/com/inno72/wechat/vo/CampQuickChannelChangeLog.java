package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 快速通道票兑换日志表
 */
@Document
public class CampQuickChannelChangeLog implements Serializable {
    private static final long serialVersionUID = -3656734658082099894L;
    @Id
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 快速通道id
     */
    private String quickChannelId;
    /**
     * 创建时间
     */
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getQuickChannelId() {
        return quickChannelId;
    }

    public void setQuickChannelId(String quickChannelId) {
        this.quickChannelId = quickChannelId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
