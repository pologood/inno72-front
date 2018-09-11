package com.inno72.vo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;

@Document
public class ZhiLianVerifivation implements Serializable {

    private static final long serialVersionUID = -2034213784600348111L;
    @Id
    private String id;

    private String code;
    /**
     * 0可用，1不可用
     */
    private Integer status;

    public static Integer STATUS_USABLE = 0;
    public static Integer STATUS_UNUSABLE = 1;

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

    public Integer getStatus() {
        return status;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }
}
