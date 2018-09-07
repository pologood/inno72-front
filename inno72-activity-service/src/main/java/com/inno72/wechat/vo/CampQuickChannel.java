package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 快速通道票
 */
@Document
public class CampQuickChannel implements Serializable {
    private static final long serialVersionUID = -4087634394791159427L;
    @Id
    private String id;
    private String userId;
    /**
     * 只存几号
     */
    private Integer date;
    /**
     * 快速通道票个数
     */
    private Integer quickChannelSize;

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

    public Integer getDate() {
        return date;
    }

    public void setDate(Integer date) {
        this.date = date;
    }

    public Integer getQuickChannelSize() {
        return quickChannelSize;
    }

    public void setQuickChannelSize(Integer quickChannelSize) {
        this.quickChannelSize = quickChannelSize;
    }
}
