package com.inno72.model;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Table(name = "inno72_interact_machine_time")
public class Inno72InteractMachineTime implements Serializable {
    private static final long serialVersionUID = 6501818024329527138L;
    /**
     * ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 活动ID
     */
    @Column(name = "interact_machine_id")
    private String interactMachineId;

    /**
     * 开始时间
     */
    @Column(name = "start_time")
    private LocalDateTime startTime;

    /**
     * 结束时间
     */
    @Column(name = "end_time")
    private LocalDateTime endTime;


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
     * 获取活动ID
     *
     * @return interact_machine_id - 活动ID
     */
    public String getInteractMachineId() {
        return interactMachineId;
    }

    /**
     * 设置活动ID
     *
     * @param interactMachineId 活动ID
     */
    public void setInteractMachineId(String interactMachineId) {
        this.interactMachineId = interactMachineId;
    }

    /**
     * 获取开始时间
     *
     * @return start_time - 开始时间
     */
    public LocalDateTime getStartTime() {
        return startTime;
    }

    /**
     * 设置开始时间
     *
     * @param startTime 开始时间
     */
    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    /**
     * 获取结束时间
     *
     * @return end_time - 结束时间
     */
    public LocalDateTime getEndTime() {
        return endTime;
    }

    /**
     * 设置结束时间
     *
     * @param endTime 结束时间
     */
    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

}