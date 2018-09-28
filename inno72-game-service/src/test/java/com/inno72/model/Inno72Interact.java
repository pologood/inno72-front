package com.inno72.model;

import java.util.Date;
import javax.persistence.*;

@Table(name = "inno72_interact")
public class Inno72Interact {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 互动名称
     */
    private String name;

    /**
     * 关联游戏
     */
    @Column(name = "game_id")
    private String gameId;

    /**
     * 活动时长
     */
    private Integer day;

    /**
     * 活动负责人
     */
    private String manager;

    /**
     * 状态：0待提交，1已提交，3已结束
     */
    private Integer status;

    /**
     * 同一用户参与活动次数
     */
    private Integer times;

    /**
     * 同一用户每一天参与活动次数
     */
    @Column(name = "day_times")
    private Integer dayTimes;

    /**
     * 同一用户获得商品次数
     */
    private Integer number;

    /**
     * 同一用户每天获得商品次数
     */
    @Column(name = "day_number")
    private Integer dayNumber;

    @Column(name = "is_delete")
    private Integer isDelete;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private String createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    private Date createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private String updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    private Date updateTime;

    /**
     * @return id
     */
    public String getId() {
        return id;
    }

    /**
     * @param id
     */
    public void setId(String id) {
        this.id = id;
    }

    /**
     * 获取互动名称
     *
     * @return name - 互动名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置互动名称
     *
     * @param name 互动名称
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * 获取关联游戏
     *
     * @return game_id - 关联游戏
     */
    public String getGameId() {
        return gameId;
    }

    /**
     * 设置关联游戏
     *
     * @param gameId 关联游戏
     */
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    /**
     * 获取活动时长
     *
     * @return day - 活动时长
     */
    public Integer getDay() {
        return day;
    }

    /**
     * 设置活动时长
     *
     * @param day 活动时长
     */
    public void setDay(Integer day) {
        this.day = day;
    }

    /**
     * 获取活动负责人
     *
     * @return manager - 活动负责人
     */
    public String getManager() {
        return manager;
    }

    /**
     * 设置活动负责人
     *
     * @param manager 活动负责人
     */
    public void setManager(String manager) {
        this.manager = manager;
    }

    /**
     * 获取状态：0待提交，1已提交，3已结束
     *
     * @return status - 状态：0待提交，1已提交，3已结束
     */
    public Integer getStatus() {
        return status;
    }

    /**
     * 设置状态：0待提交，1已提交，3已结束
     *
     * @param status 状态：0待提交，1已提交，3已结束
     */
    public void setStatus(Integer status) {
        this.status = status;
    }

    /**
     * 获取同一用户参与活动次数
     *
     * @return times - 同一用户参与活动次数
     */
    public Integer getTimes() {
        return times;
    }

    /**
     * 设置同一用户参与活动次数
     *
     * @param times 同一用户参与活动次数
     */
    public void setTimes(Integer times) {
        this.times = times;
    }

    /**
     * 获取同一用户每一天参与活动次数
     *
     * @return day_times - 同一用户每一天参与活动次数
     */
    public Integer getDayTimes() {
        return dayTimes;
    }

    /**
     * 设置同一用户每一天参与活动次数
     *
     * @param dayTimes 同一用户每一天参与活动次数
     */
    public void setDayTimes(Integer dayTimes) {
        this.dayTimes = dayTimes;
    }

    /**
     * 获取同一用户获得商品次数
     *
     * @return number - 同一用户获得商品次数
     */
    public Integer getNumber() {
        return number;
    }

    /**
     * 设置同一用户获得商品次数
     *
     * @param number 同一用户获得商品次数
     */
    public void setNumber(Integer number) {
        this.number = number;
    }

    /**
     * 获取同一用户每天获得商品次数
     *
     * @return day_number - 同一用户每天获得商品次数
     */
    public Integer getDayNumber() {
        return dayNumber;
    }

    /**
     * 设置同一用户每天获得商品次数
     *
     * @param dayNumber 同一用户每天获得商品次数
     */
    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    /**
     * @return is_delete
     */
    public Integer getIsDelete() {
        return isDelete;
    }

    /**
     * @param isDelete
     */
    public void setIsDelete(Integer isDelete) {
        this.isDelete = isDelete;
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
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * 设置创建时间
     *
     * @param createTime 创建时间
     */
    public void setCreateTime(Date createTime) {
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
    public Date getUpdateTime() {
        return updateTime;
    }

    /**
     * 设置更新时间
     *
     * @param updateTime 更新时间
     */
    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}