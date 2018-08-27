package com.inno72.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.util.Date;
@Document
public class GoodFather implements Serializable {
    private static final long serialVersionUID = 6901042169292733902L;
    @Id
    public String id;

    public String weboId;//微博id

    public String taoboId;//淘宝id

    public String name;//姓名

    public String phone;//手机号

    public Integer status;//0未参与抽奖，1抽奖中，2已经中奖

    public static Integer STATUS_UNATTEND=0; //未参与抽奖

    public static Integer STATUS_IN_LOTTERY_DRAW=1; //抽奖中

    public static Integer STATUS_HIT=2; //中奖

    @DateTimeFormat(iso=DateTimeFormat.ISO.DATE_TIME)
    public Date createTime;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWeboId() {
        return weboId;
    }

    public void setWeboId(String weboId) {
        this.weboId = weboId;
    }

    public String getTaoboId() {
        return taoboId;
    }

    public void setTaoboId(String taoboId) {
        this.taoboId = taoboId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
