package com.inno72.machine.model;

import com.inno72.machine.vo.CityVo;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import java.util.List;

public class Inno72AdminArea {
    /**
     * 区域code
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String code;

    /**
     * 父code
     */
    @Column(name = "parent_code")
    private String parentCode;

    /**
     * 名称
     */
    @Column(name="name")
    private String name;

    /**
     * 省
     */
    @Column(name="province")
    private String province;

    /**
     * 城市
     */
    @Column(name="city")
    private String city;

    /**
     * 地区
     */
    @Column(name="district")
    private String district;

    /**
     * 商圈
     */
    @Column(name="circle")
    private String circle;

    /**
     * 级别
     */
    @Column(name="level")
    private int level;

    private List<CityVo> cityVoList;

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

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getCircle() {
        return circle;
    }

    public void setCircle(String circle) {
        this.circle = circle;
    }

    public int getLevel() {
        return level;
    }

    public List<CityVo> getCityVoList() {
        return cityVoList;
    }

    public void setCityVoList(List<CityVo> cityVoList) {
        this.cityVoList = cityVoList;
    }

    public void setLevel(int level) {
        this.level = level;
    }


}
