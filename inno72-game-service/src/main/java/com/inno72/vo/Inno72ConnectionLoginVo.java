package com.inno72.vo;

import java.io.Serializable;

public class Inno72ConnectionLoginVo  extends Inno72ConnectionBaseResultVo implements Serializable {
    private static final long serialVersionUID = 8791245881128521152L;
    private String userNick;
    private Boolean canOrder;
    private Boolean countGoods;
    private String userId;

    public String getUserNick() {
        return userNick;
    }

    public void setUserNick(String userNick) {
        this.userNick = userNick;
    }

    public Boolean getCanOrder() {
        return canOrder;
    }

    public void setCanOrder(Boolean canOrder) {
        this.canOrder = canOrder;
    }

    public Boolean getCountGoods() {
        return countGoods;
    }

    public void setCountGoods(Boolean countGoods) {
        this.countGoods = countGoods;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
