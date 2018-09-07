package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 抽奖次数兑换日志
 */
@Document
public class CampAwardsChangeLog implements Serializable {
    private static final long serialVersionUID = 991883503729146973L;
    @Id
    private String id;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 场次
     */
    private Integer timesCode;
    /**
     * 兑奖次数
     */
    private Integer awardsSize;
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

    public Integer getTimesCode() {
        return timesCode;
    }

    public void setTimesCode(Integer timesCode) {
        this.timesCode = timesCode;
    }

    public Integer getAwardsSize() {
        return awardsSize;
    }

    public void setAwardsSize(Integer awardsSize) {
        this.awardsSize = awardsSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
