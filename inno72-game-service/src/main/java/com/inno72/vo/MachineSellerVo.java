package com.inno72.vo;

import java.io.Serializable;

/**
 *
 */
public class MachineSellerVo implements Serializable {
    private static final long serialVersionUID = 175479512480858039L;
    private String machineId;
    private String sellerId;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getSellerId() {
        return sellerId;
    }

    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }
}
