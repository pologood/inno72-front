package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 积分兑换日志
 */
@Document
public class CampScoreChangeLog implements Serializable {

    private static final long serialVersionUID = -4664669338288159315L;
    @Id
    private String id;
    private String userId;
    private Integer timesCode;
    /**
     * 兑换前积分
     */
    private Integer startScore;
    /**
     * 兑换后积分
     */
    private Integer endScore;
    /**
     * 兑换前兑奖次数
     */
    private Integer startAwardsSize;
    /**
     * 兑换后兑奖次数
     */
    private Integer endAwardsSize;
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

    public Integer getStartScore() {
        return startScore;
    }

    public void setStartScore(Integer startScore) {
        this.startScore = startScore;
    }

    public Integer getEndScore() {
        return endScore;
    }

    public void setEndScore(Integer endScore) {
        this.endScore = endScore;
    }

    public Integer getStartAwardsSize() {
        return startAwardsSize;
    }

    public void setStartAwardsSize(Integer startAwardsSize) {
        this.startAwardsSize = startAwardsSize;
    }

    public Integer getEndAwardsSize() {
        return endAwardsSize;
    }

    public void setEndAwardsSize(Integer endAwardsSize) {
        this.endAwardsSize = endAwardsSize;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTimesCode() {
        return timesCode;
    }

    public void setTimesCode(Integer timesCode) {
        this.timesCode = timesCode;
    }
}
