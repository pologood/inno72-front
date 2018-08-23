package com.inno72.machine.vo;

import java.util.List;

public class DistrictVo {
    private String code;

    /**
     * 父code
     */
    private String parentCode;

    /**
     * 名称
     */
    private String name;

    /**
     * 级别
     */
    private int level;

    private List<CircleVo> circleVoList;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getParentCode() {
        return parentCode;
    }

    public void setParentCode(String parentCode) {
        this.parentCode = parentCode;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public List<CircleVo> getCircleVoList() {
        return circleVoList;
    }

    public void setCircleVoList(List<CircleVo> circleVoList) {
        this.circleVoList = circleVoList;
    }
}
