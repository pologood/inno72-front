package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;

/**
 * 用户任务表
 */
@Document
public class CampUserTask implements Serializable {
    private static final long serialVersionUID = 3967356902217902493L;
    @Id
    private String id;

    private String userId;
    /**
     * 是否主线任务
     */
    private Integer mainFlag;
    public static Integer MAINFLAG_MAIN =1;
    public static Integer MAINFLAG_NOT_MAIN =0;

    private String taskId;
    /**
     * 此用户的teamCode
     */
    private Integer teamCode;
    /**
     * 场次编号
     */
    private Integer timesCode;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

    public Integer getTimesCode() {
        return timesCode;
    }

    public void setTimesCode(Integer timesCode) {
        this.timesCode = timesCode;
    }

    public Integer getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(Integer mainFlag) {
        this.mainFlag = mainFlag;
    }
}
