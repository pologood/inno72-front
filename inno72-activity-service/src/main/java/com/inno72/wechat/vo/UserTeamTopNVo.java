package com.inno72.wechat.vo;

import java.io.Serializable;
import java.util.List;

/**
 * topN vo
 */
public class UserTeamTopNVo implements Serializable {
    private static final long serialVersionUID = 1336194894914474771L;
    /**
     * 我的排名
     */
    private Integer ranking;
    /**
     * 阵营编码
     */
    private Integer teamCode;
    /**
     * top5数据
     */
    private List<TopNVo> list;

    public Integer getRanking() {
        return ranking;
    }

    public void setRanking(Integer ranking) {
        this.ranking = ranking;
    }

    public Integer getTeamCode() {
        return teamCode;
    }

    public void setTeamCode(Integer teamCode) {
        this.teamCode = teamCode;
    }

    public List<TopNVo> getList() {
        return list;
    }

    public void setList(List<TopNVo> list) {
        this.list = list;
    }
}
