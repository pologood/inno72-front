package com.inno72.model;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Table(name = "inno72_activity_plan_game_result")
public class Inno72ActivityPlanGameResult {
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
     * 奖品id （商品/优惠券)
     */
    @Column(name = "prize_id")
    private String prizeId;

    /**
     * 奖品关联类型（1商品，2优惠券）
     */
    @Column(name = "prize_type")
    private String prizeType;

    /**
     * 结果code
     */
    @Column(name = "result_code")
    private Integer resultCode;

    /**
     * 结果code描述
     */
    @Column(name = "result_remark")
    private String resultRemark;

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
     * 获取奖品id （商品/优惠券)
     *
     * @return prize_id - 奖品id （商品/优惠券)
     */
    public String getPrizeId() {
        return prizeId;
    }

    /**
     * 设置奖品id （商品/优惠券)
     *
     * @param prizeId 奖品id （商品/优惠券)
     */
    public void setPrizeId(String prizeId) {
        this.prizeId = prizeId;
    }

    /**
     * 获取奖品关联类型（1商品，2优惠券）
     *
     * @return prize_type - 奖品关联类型（1商品，2优惠券）
     */
    public String getPrizeType() {
        return prizeType;
    }

    /**
     * 设置奖品关联类型（1商品，2优惠券）
     *
     * @param prizeType 奖品关联类型（1商品，2优惠券）
     */
    public void setPrizeType(String prizeType) {
        this.prizeType = prizeType;
    }

    /**
     * 获取结果code
     *
     * @return result_code - 结果code
     */
    public Integer getResultCode() {
        return resultCode;
    }

    /**
     * 设置结果code
     *
     * @param resultCode 结果code
     */
    public void setResultCode(Integer resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * 获取结果code描述
     *
     * @return result_remark - 结果code描述
     */
    public String getResultRemark() {
        return resultRemark;
    }

    /**
     * 设置结果code描述
     *
     * @param resultRemark 结果code描述
     */
    public void setResultRemark(String resultRemark) {
        this.resultRemark = resultRemark;
    }
}