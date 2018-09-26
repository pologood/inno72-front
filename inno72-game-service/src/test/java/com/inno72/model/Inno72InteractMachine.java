package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_interact_machine")
public class Inno72InteractMachine {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 活动排期ID
     */
    @Column(name = "interact_id")
    private String interactId;

    /**
     * 机器ID
     */
    @Column(name = "machine_id")
    private String machineId;

    @Column(name = "machine_code")
    private String machineCode;

    /**
     * 获取ID
     *
     * @return id - ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置ID
     *
     * @param id ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取活动排期ID
     *
     * @return interact_id - 活动排期ID
     */
    public String getInteractId() {
        return interactId;
    }

    /**
     * 设置活动排期ID
     *
     * @param interactId 活动排期ID
     */
    public void setInteractId(String interactId) {
        this.interactId = interactId;
    }

    /**
     * 获取机器ID
     *
     * @return machine_id - 机器ID
     */
    public String getMachineId() {
        return machineId;
    }

    /**
     * 设置机器ID
     *
     * @param machineId 机器ID
     */
    public void setMachineId(String machineId) {
        this.machineId = machineId;
    }

    /**
     * @return machine_code
     */
    public String getMachineCode() {
        return machineCode;
    }

    /**
     * @param machineCode
     */
    public void setMachineCode(String machineCode) {
        this.machineCode = machineCode;
    }
}