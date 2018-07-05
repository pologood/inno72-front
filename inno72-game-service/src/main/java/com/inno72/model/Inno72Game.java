package com.inno72.model;

import java.time.LocalDateTime;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.inno72.common.CustomLocalDateTimeSerializer;

@Table(name = "inno72_game")
public class Inno72Game {
	
    @Column(name = "Id")
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY,generator = "select uuid()")
    private String id;

    /**
     * 游戏版本
     */
    private String version;

    /**
     * 游戏描述
     */
    @Column(name = "`desc`")
    private String desc;

    /**
     * 售卖者id
     */
    @Column(name = "seller_id")
    private Long sellerId;

    /**
     * 店铺ID
     */
    @Column(name = "shop_id")
    private Long shopId;

    /**
     * 商家设定的奖池所对应的ID
     */
    @Column(name = "interact_id")
    private String interactId;

    /**
     * 每天最大参与次数(-1表示不限制数量)
     */
    @Column(name = "max_participance_per_day")
    private Boolean maxParticipancePerDay;

    /**
     * 最大参与次数(-1表示不限制数量)
     */
    @Column(name = "max_participance_total")
    private Integer maxParticipanceTotal;

    /**
     * 每天最大奖品数量(-1表示不限制数量
)
     */
    @Column(name = "max_prize_per_day")
    private Integer maxPrizePerDay;

    /**
     * 最大奖品数量(-1表示不限制数量)
     */
    @Column(name = "max_prize_total")
    private Integer maxPrizeTotal;

    /**
     * 创建人
     */
    @Column(name = "create_id")
    private Long createId;

    /**
     * 创建时间
     */
    @Column(name = "create_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime createTime;

    /**
     * 更新人
     */
    @Column(name = "update_id")
    private Long updateId;

    /**
     * 更新时间
     */
    @Column(name = "update_time")
    @JsonSerialize(using = CustomLocalDateTimeSerializer.class)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime updateTime;

    /**
     * 活动id
     */
    @Column(name = "activity_id")
    private Long activityId;

    /**
     * @return Id
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
     * 获取游戏版本
     *
     * @return version - 游戏版本
     */
    public String getVersion() {
        return version;
    }

    /**
     * 设置游戏版本
     *
     * @param version 游戏版本
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * 获取游戏描述
     *
     * @return desc - 游戏描述
     */
    public String getDesc() {
        return desc;
    }

    /**
     * 设置游戏描述
     *
     * @param desc 游戏描述
     */
    public void setDesc(String desc) {
        this.desc = desc;
    }


    /**
     * 获取售卖者id
     *
     * @return seller_id - 售卖者id
     */
    public Long getSellerId() {
        return sellerId;
    }

    /**
     * 设置售卖者id
     *
     * @param sellerId 售卖者id
     */
    public void setSellerId(Long sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * 获取店铺ID
     *
     * @return shop_id - 店铺ID
     */
    public Long getShopId() {
        return shopId;
    }

    /**
     * 设置店铺ID
     *
     * @param shopId 店铺ID
     */
    public void setShopId(Long shopId) {
        this.shopId = shopId;
    }

    /**
     * 获取商家设定的奖池所对应的ID
     *
     * @return interact_id - 商家设定的奖池所对应的ID
     */
    public String getInteractId() {
        return interactId;
    }

    /**
     * 设置商家设定的奖池所对应的ID
     *
     * @param interactId 商家设定的奖池所对应的ID
     */
    public void setInteractId(String interactId) {
        this.interactId = interactId;
    }

    /**
     * 获取每天最大参与次数(-1表示不限制数量)
     *
     * @return max_participance_per_day - 每天最大参与次数(-1表示不限制数量)
     */
    public Boolean getMaxParticipancePerDay() {
        return maxParticipancePerDay;
    }

    /**
     * 设置每天最大参与次数(-1表示不限制数量)
     *
     * @param maxParticipancePerDay 每天最大参与次数(-1表示不限制数量)
     */
    public void setMaxParticipancePerDay(Boolean maxParticipancePerDay) {
        this.maxParticipancePerDay = maxParticipancePerDay;
    }

    /**
     * 获取最大参与次数(-1表示不限制数量)
     *
     * @return max_participance_total - 最大参与次数(-1表示不限制数量)
     */
    public Integer getMaxParticipanceTotal() {
        return maxParticipanceTotal;
    }

    /**
     * 设置最大参与次数(-1表示不限制数量)
     *
     * @param maxParticipanceTotal 最大参与次数(-1表示不限制数量)
     */
    public void setMaxParticipanceTotal(Integer maxParticipanceTotal) {
        this.maxParticipanceTotal = maxParticipanceTotal;
    }

    /**
     * 获取每天最大奖品数量(-1表示不限制数量
)
     *
     * @return max_prize_per_day - 每天最大奖品数量(-1表示不限制数量
)
     */
    public Integer getMaxPrizePerDay() {
        return maxPrizePerDay;
    }

    /**
     * 设置每天最大奖品数量(-1表示不限制数量
)
     *
     * @param maxPrizePerDay 每天最大奖品数量(-1表示不限制数量
)
     */
    public void setMaxPrizePerDay(Integer maxPrizePerDay) {
        this.maxPrizePerDay = maxPrizePerDay;
    }

    /**
     * 获取最大奖品数量(-1表示不限制数量)
     *
     * @return max_prize_total - 最大奖品数量(-1表示不限制数量)
     */
    public Integer getMaxPrizeTotal() {
        return maxPrizeTotal;
    }

    /**
     * 设置最大奖品数量(-1表示不限制数量)
     *
     * @param maxPrizeTotal 最大奖品数量(-1表示不限制数量)
     */
    public void setMaxPrizeTotal(Integer maxPrizeTotal) {
        this.maxPrizeTotal = maxPrizeTotal;
    }

    /**
     * 获取创建人
     *
     * @return create_id - 创建人
     */
    public Long getCreateId() {
        return createId;
    }

    /**
     * 设置创建人
     *
     * @param createId 创建人
     */
    public void setCreateId(Long createId) {
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
    public Long getUpdateId() {
        return updateId;
    }

    /**
     * 设置更新人
     *
     * @param updateId 更新人
     */
    public void setUpdateId(Long updateId) {
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

    /**
     * 获取活动id
     *
     * @return activity_id - 活动id
     */
    public Long getActivityId() {
        return activityId;
    }

    /**
     * 设置活动id
     *
     * @param activityId 活动id
     */
    public void setActivityId(Long activityId) {
        this.activityId = activityId;
    }
}