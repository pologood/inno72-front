package com.inno72.model;

import javax.persistence.*;
import java.time.LocalDateTime;

@Table(name = "inno72_activity_plan")
public class Inno72ActivityPlan {
    /**
     * 活动排期ID
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 活动ID
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 活动ID
     */
    @Column(name = "plan_code")
    private String planCode;

    /**
     * 游戏ID
     */
    @Column(name = "game_id")
    private String gameId;

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
     * 状态：0正常，1删除
     */
    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 备注描述
     */
    private String remark;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private LocalDateTime updateTime;


    @Column(name = "user_max_times")
    private Integer userMaxTimes;

    /**
     * 获取活动排期ID
     *
     * @return id - 活动排期ID
     */
    public String getId() {
        return id;
    }

    /**
     * 设置活动排期ID
     *
     * @param id 活动排期ID
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取活动ID
     *
     * @return activity_id - 活动ID
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * 设置活动ID
     *
     * @param activityId 活动ID
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /**
     * 获取游戏ID
     *
     * @return game_id - 游戏ID
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * 设置游戏ID
     *
     * @param gameId 游戏ID
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
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

    /**
     * 获取状态：0正常，1删除
     *
     * @return is_delete - 状态：0正常，1删除
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * 设置状态：0正常，1删除
     *
     * @param isDelete 状态：0正常，1删除
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
    }

    /**
     * 获取备注描述
     *
     * @return remark - 备注描述
     */
    public String getRemark() {
        return remark;
    }

    /**
     * 设置备注描述
     *
     * @param remark 备注描述
     */
    public void setRemark(String remark) {
        this.remark = remark;
    }

    /**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public String getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(String createId) {
        this.createId = createId;
    }

    /**
     * 获取创建时间
     *
     * @return create_time - 创建时间
     */
    public LocalDateTime getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(LocalDateTime createTime) {
        this.createTime = createTime;
    }

    /**
     * 获取更新人
     *
     * @return update_id - 更新人
     */
    public String getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(String updateId) {
        this.updateId = updateId;
    }

    /**
     * 获取更新时间
     *
     * @return update_time - 更新时间
     */
    public LocalDateTime getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(LocalDateTime updateTime) {
        this.updateTime = updateTime;
    }

	public Integer getUserMaxTimes() {
		return userMaxTimes;
	}

	public void setUserMaxTimes(Integer userMaxTimes) {
		this.userMaxTimes = userMaxTimes;
	}

	public String getPlanCode() {
		return planCode;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
	}
}