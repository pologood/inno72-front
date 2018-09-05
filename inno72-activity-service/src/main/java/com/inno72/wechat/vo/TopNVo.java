package com.inno72.wechat.vo;

import java.io.Serializable;

public class TopNVo implements Serializable {
    private static final long serialVersionUID = 6994308015820428775L;
    /**
     * 排名1-5
     */
    private Integer ranking;
    /**
     * 分数
     */
    private Integer score;

    /**
     * 微信昵称
     */
    private String nickName;
    /**
     * 阵营编码
     */
    private Integer teamCode;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getScore() {
        return score;
    }

    public void setScore(Integer score) {
        this.score = score;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

}
