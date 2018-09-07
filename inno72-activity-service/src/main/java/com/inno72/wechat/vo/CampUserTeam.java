package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户阵营中间表
 */
@Document
public class CampUserTeam implements Serializable {
    private static final long serialVersionUID = 2374308779416183803L;
    @Id
    private String id;
    /**
     * 阵营id
     */
    private String teamId;
    /**
     * 阵营code，冗余字段
     */
    private Integer teamCode;
    /**
     * 分数
     */
    private Integer score;
    /**
     * 兑奖次数
     */
    private Integer awardsSize;
    /**
     * 场次编码
     */
    private Integer timesCode;
    /**
     * 用户id
     */
    private String userId;
    /**
     * 昵称
     */
    private String nickName;
    /**
     * openid
     */
//    private String openid;

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

    public String getTeamId() {
        return teamId;
    }

    public void setTeamId(String teamId) {
        this.teamId = teamId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

//    public String getOpenid() {
//        return openid;
//    }
//
//    public void setOpenid(String openid) {
//        this.openid = openid;
//    }

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

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getAwardsSize() {
        return awardsSize;
    }

    public void setAwardsSize(Integer awardsSize) {
        this.awardsSize = awardsSize;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
