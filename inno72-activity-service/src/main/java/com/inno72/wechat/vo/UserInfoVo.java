package com.inno72.wechat.vo;

import java.io.Serializable;

/**
 * 获取用户信息实体类
 */
public class UserInfoVo implements Serializable {
    private static final long serialVersionUID = 7515938744876599281L;
    /**
     * 得分
     */
    private Integer score;
    /**
     * 阵营编号：0未选择阵营，1力量，2智慧，3灵力
     */
    private Integer teamCode;
    /**
     *我的快速通道票个数（0无）
     */
    private Integer quickChannelSize;
    /**
     * 游戏是否开始0未开始，1进行中
     */
    private Integer gameFlag;

    private String nickName;

    public static Integer GAMEFLAG_UNSTARTED=0;

    public static Integer GAMEFLAG_STARTED=1;

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

    public Integer getQuickChannelSize() {
        return quickChannelSize;
    }

    public void setQuickChannelSize(Integer quickChannelSize) {
        this.quickChannelSize = quickChannelSize;
    }

    public Integer getGameFlag() {
        return gameFlag;
    }

    public void setGameFlag(Integer gameFlag) {
        this.gameFlag = gameFlag;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }
}
