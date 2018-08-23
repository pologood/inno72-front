package com.inno72.machine.vo;

import java.util.List;

public class CityVo {
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

    List<DistrictVo> districtVoList;

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

    public List<DistrictVo> getDistrictVoList() {
        return districtVoList;
    }

    public void setDistrictVoList(List<DistrictVo> districtVoList) {
        this.districtVoList = districtVoList;
    }
}
