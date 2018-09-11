package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 任务表
 */
@Document
public class CampTask implements Serializable {
    private static final long serialVersionUID = -2456829467185340942L;
    @Id
    private String id;
    /**
     * 任务类型：1大，2中，3小
     */
    private Integer type;

    public static Integer TYPE_BIG = 1;
    public static Integer TYPE_MID = 2;
    public static Integer TYPE_LITTLE = 3;
    /**
     * 任务分数
     */
    private Integer score;
//    public static Integer SCORE_BIG = 85;
//    public static Integer SCORE_MID = 50;
//    public static Integer SCORE_LITTLE = 20;
    /**
     * 任务所属阵营
     */
    private Integer teamCode;
    /**
     * 任务描述
     */
    private String description;
    /**
     * 是否主线任务 0非主线，1主线
     */
    @Transient
    private Integer mainFlag;
    public static Integer MAINFLAG_MAIN =1;
    public static Integer MAINFLAG_NOT_MAIN =0;
    /**
     * 是否已经完成 0未完成，1已经完成
     */
    @Transient
    private Integer finishFlag;
    public static Integer FINISHFLAG_FINISH =1;
    public static Integer FINISHFLAG_UNFINISH =0;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getMainFlag() {
        return mainFlag;
    }

    public void setMainFlag(Integer mainFlag) {
        this.mainFlag = mainFlag;
    }

    public Integer getFinishFlag() {
        return finishFlag;
    }

    public void setFinishFlag(Integer finishFlag) {
        this.finishFlag = finishFlag;
    }
}
