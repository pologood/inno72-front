package com.inno72.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class ZhiLianUser implements Serializable {

    private static final long serialVersionUID = 2166948359308989637L;

    @Id
    private String id;

    private String code;

    private String userId;

    private Long createTime;
    /**
     * 0未玩过
     * 1已经玩过
     */
    private Integer status;

    public static Integer STATUS_DOWN = 1;

    public static Integer STATUS_UNDOWN = 0;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Long createTime) {
        this.createTime = createTime;
    }

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
