package com.inno72.machine.vo;

import java.io.Serializable;

public class SupplyRequestVo implements Serializable {

    private static final long serialVersionUID = -6486293122447494756L;
    private String machineId;

    private String goodsId;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getGoodsId() {
        return goodsId;
    }

    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }
}
