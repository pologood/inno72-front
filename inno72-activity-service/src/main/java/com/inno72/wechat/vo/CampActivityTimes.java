package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 活动场次表
 */
@Document
public class CampActivityTimes implements Serializable {

    private static final long serialVersionUID = 3484195086056056255L;
    @Id
    private String id;

    /**
     * 场次名称
     */
    private String timesName;
    /**
     * 场次编码（1，2，3，4）
     */
    private Integer timesCode;
    /**
     * 开始时间
     */
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private Date startTime;
    /**
     * 结束时间
     */
    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    private Date endTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getTimesName() {
        return timesName;
    }

    public void setTimesName(String timesName) {
        this.timesName = timesName;
    }

    public Integer getTimesCode() {
        return timesCode;
    }

    public void setTimesCode(Integer timesCode) {
        this.timesCode = timesCode;
    }
}
