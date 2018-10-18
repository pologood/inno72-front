package com.inno72.model;

import javax.persistence.*;

@Table(name = "inno72_interact_goods")
public class Inno72InteractGoods {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private String id;

    /**
     * 互动ID
     */
    @Column(name = "interact_id")
    private String interactId;

    /**
     * 店铺ID
     */
    @Column(name = "goods_id")
    private String goodsId;

    /**
     * 同一用户每天获得数量
     */
    @Column(name = "user_day_number")
    private Integer userDayNumber;

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
     * 获取互动ID
     *
     * @return interact_id - 互动ID
     */
    public String getInteractId() {
        return interactId;
    }

    /**
     * 设置互动ID
     *
     * @param interactId 互动ID
     */
    public void setInteractId(String interactId) {
        this.interactId = interactId;
    }

    /**
     * 获取店铺ID
     *
     * @return goods_id - 店铺ID
     */
    public String getGoodsId() {
        return goodsId;
    }

    /**
     * 设置店铺ID
     *
     * @param goodsId 店铺ID
     */
    public void setGoodsId(String goodsId) {
        this.goodsId = goodsId;
    }

    /**
     * 获取同一用户每天获得数量
     *
     * @return user_day_number - 同一用户每天获得数量
     */
    public Integer getUserDayNumber() {
        return userDayNumber;
    }

    /**
     * 设置同一用户每天获得数量
     *
     * @param userDayNumber 同一用户每天获得数量
     */
    public void setUserDayNumber(Integer userDayNumber) {
        this.userDayNumber = userDayNumber;
    }
}