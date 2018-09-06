package com.inno72.wechat.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

/**
 * 阵营战队表（三大阵营）
 */
@Document
public class CampTeam implements Serializable {
    private static final long serialVersionUID = 6929821842923507023L;
    @Id
    private String id;
    /**
     * 阵营编号：1力量，2智慧，3灵力
     */
    private Integer teamCode;

    public static Integer TEAMCODE_POWER = 1;
    public static Integer TEAMCODE_WISDOM = 2;
    public static Integer TEAMCODE_MANA = 3;
    /**
     * 阵营名称
     */
    private String name;
    /**
     * 场次名称
     */
    private String timesName;
    /**
     * 场次编码（1，2，3，4）
     */
    private Integer timesCode;
    /**
     * 分数
     */
    private Integer score;


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
