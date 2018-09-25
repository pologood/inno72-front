package com.inno72.check.vo;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Transient;

import com.inno72.check.model.Inno72CheckSignIn;

public class MachineSignInVo {

    private String machineId;

    /**
     * 机器code
     */
    private String machineCode;

    /**
     * 机器名称
     */
    @Column(name = "machine_name")
    private String machineName;

    @Transient
    private String localeStr;

    @Transient
    private int signInStatus;

    private List<Inno72CheckSignIn> signInList;

    public String getMachineId() {
        return machineId;
    }

    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    public String getMachineCode() {
        return machineCode;
    }

    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }

    public String getMachineName() {
        return machineName;
    }

    public void setMachineName(String machineName) {
        this.machineName = machineName;
    }

    public String getLocaleStr() {
        return localeStr;
    }

    public void setLocaleStr(String localeStr) {
        this.localeStr = localeStr;
    }

    public int getSignInStatus() {
        return signInStatus;
    }

    public void setSignInStatus(int signInStatus) {
        this.signInStatus = signInStatus;
    }

    public List<Inno72CheckSignIn> getSignInList() {
        return signInList;
    }

    public void setSignInList(List<Inno72CheckSignIn> signInList) {
        this.signInList = signInList;
    }
}
