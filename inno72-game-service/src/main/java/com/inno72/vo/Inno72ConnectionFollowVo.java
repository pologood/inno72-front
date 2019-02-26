package com.inno72.vo;

import java.io.Serializable;

public class Inno72ConnectionFollowVo extends Inno72ConnectionBaseResultVo implements Serializable {
    private static final long serialVersionUID = 5731421662057098382L;
    private Integer flag=1;

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }
}
