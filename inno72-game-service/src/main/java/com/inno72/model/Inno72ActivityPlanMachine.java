package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_activity_plan_machine")
public class Inno72ActivityPlanMachine {
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 活动排期ID
     */
    @Column(name = "activity_plan_id")
    private String activityPlanId;

    /**
     * 机器ID
     */
    @Column(name = "machine_id")
    private String machineId;

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
     * @return activity_plan_id - 活动排期ID
     */
    public String getActivityPlanId() {
        return activityPlanId;
    }

    /**
     * 设置活动排期ID
     *
     * @param activityPlanId 活动排期ID
     */
    public void setActivityPlanId(String activityPlanId) {
        this.activityPlanId = activityPlanId;
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
}