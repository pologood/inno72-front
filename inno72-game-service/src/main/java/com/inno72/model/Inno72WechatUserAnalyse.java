package com.inno72.model;

import javax.persistence.*;
import java.util.Date;

@Table(name = "inno72_wechat_user_analyse")
public class Inno72WechatUserAnalyse {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "select uuid()")
    private String id;

    /**
     * 微信appId
     */
    @Column(name = "seller_id")
    private String sellerId;

    @Column(name = "game_user_channel_id")
    private String gameUserChannelId;

    /**
     * 活动id
     */
    @Column(name = "activity_id")
    private String activityId;

    /**
     * 微信用户标识
     */
    @Column(name = "open_id")
    private String openId;

    @Column(name = "create_time")
    private Date createTime;

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
     * 获取微信appId
     *
     * @return seller_id - 微信appId
     */
    public String getSellerId() {
        return sellerId;
    }

    /**
     * 设置微信appId
     *
     * @param sellerId 微信appId
     */
    public void setSellerId(String sellerId) {
        this.sellerId = sellerId;
    }

    /**
     * @return game_user_channel_id
     */
    public String getGameUserChannelId() {
        return gameUserChannelId;
    }

    /**
     * @param gameUserChannelId
     */
    public void setGameUserChannelId(String gameUserChannelId) {
        this.gameUserChannelId = gameUserChannelId;
    }

    /**
     * 获取活动id
     *
     * @return activity_id - 活动id
     */
    public String getActivityId() {
        return activityId;
    }

    /**
     * 设置活动id
     *
     * @param activityId 活动id
     */
    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    /**
     * 获取微信用户标识
     *
     * @return open_id - 微信用户标识
     */
    public String getOpenId() {
        return openId;
    }

    /**
     * 设置微信用户标识
     *
     * @param openId 微信用户标识
     */
    public void setOpenId(String openId) {
        this.openId = openId;
    }

    /**
     * @return create_time
     */
    public Date getCreateTime() {
        return createTime;
    }

    /**
     * @param createTime
     */
    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}