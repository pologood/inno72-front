package com.inno72.vo;

import java.util.List;

import lombok.Data;

@Data
public class UserSessionVo {

	private String traceId;
	private String planCode;//TODO
	private String sellerName;//TODO 品牌名称
	private String shipmentNum;//TODO 出货数量
	private String scanUrl;//TODO 扫码路径
	private String goodsName;//TODO 商品名称
	private String refOrderStatus;//TODO 订单状态
	private String scanLoginUrl;//TODO 扫码登录url
	private String scanPayUrl;//TODO 暂时没有
	private String interactId;//TODO 奖池ID
	private String gameUserId;
	private String mid;
	private String userNick;
	private String userId;
	private String accessToken;
	private String gameId;
	private String sessionUuid;
	private String mixnick;
	private String source;
	private String refOrderId;
	private String sellerId;
	private String merchantName;
	private Inno72MachineVo inno72MachineVo;

	/**
	 * 商户总ID - table -> inno72_merchant_user.merchant_id
	 */
	private String merchantAccountId;// TODO 新

	/**
	 * 商户总名称 - table -> inno72_merchant_user.merchant_name
	 */
	private String merchantAccountName;// TODO 新

	/**
	 * 渠道商家ID - table -> inno72_merchant.id
	 */
	private String channelMerchantId;//TODO 新

	/**
	 * 渠道ID //TODO, 修改为渠道商家channelName - table -> inno72_merchant.channel_name
	 */
	private String channelName;


	/**
	 * 查询游戏结果对应的货道时存入
	 */
	private String gameReport;
	/**
	 * 下单存入
	 */
	private String inno72OrderId;

	private String inno72CouponOrderId;

	private String activityPlanId;

	private Long playTimes;
	private boolean canOrder;
	private boolean canGame;
	private String channelId;
	private Integer channelType;
	private String machineId;
	private String activityId;
	private String machineCode;
	private boolean countGoods;

	private int loginType = 0;

	private List<GoodsVo> goodsList;

	/**
	 * 是否登录 true 已登录 false 没登录
	 */
	private boolean logged;

	/**
	 * 商品id
	 */
	private String goodsId;
	/**
	 * 商品编码
	 */
	private String goodsCode;

	/**
	 * 淘宝认证url
	 */
	private String authUrl;

	/**
	 * 是否需要支付
	 */
	private boolean needPay;

	/**
	 * 是否入会
	 */
	private String isVip;

	/**
	 * 入会sessionkey
	 */
	private String sessionKey;

	/**
	 * 是否扫描过的
	 */
	private boolean isScanned;
	/**
	 * 新零售入会二维码
	 */
	private String newRetailMemberUrl;
	/**
	 * 是否显示新零售入会码0不展示，1展示
	 */
	private Integer displayNewRetailMemberUrlFlag;
	/**
	 * 商品类型：0商品，1优惠券
	 */
	private Integer goodsType;
	public static final Integer GOODSTYPE_GOODS = 0;
	public static final Integer GOODSTYPE_COUPON = 1;
	public static final Integer DISPLAYNEWRETAILMEMBERURLFLAG_YES = 1;
	public static final Integer DISPLAYNEWRETAILMEMBERURLFLAG_NO = 0;

	/**
	 * 活动类型
	 * @see com.inno72.model.Inno72Activity.ActivityType
	 */
	private Integer activityType = 0;


	/**
	 * 是否关注 true 已关注 false 未关注
	 */
	private boolean fllowed;

	public UserSessionVo(String mid, String userNick, String userId, String access_token, String gameId,
			String sessionUuid, String planId) {
		super();
		this.mid = mid;
		this.userNick = userNick;
		this.userId = userId;
		this.accessToken = access_token;
		this.gameId = gameId;
		this.sessionUuid = sessionUuid;
		this.activityPlanId = planId;
	}

	public UserSessionVo() {
		super();
	}


	public void setCanOrder(boolean canOrder) {
		this.canOrder = canOrder;
	}

	public boolean getCanOrder() {
		return canOrder;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
	}

	public String getChannelId() {
		return channelId;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
	}

	public String getMachineId() {
		return machineId;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
	}

	public String getActivityId() {
		return activityId;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
	}

	public String getMachineCode() {
		return machineCode;
	}

	public void setCountGoods(boolean countGoods) {
		this.countGoods = countGoods;
	}

	public boolean getCountGoods() {
		return countGoods;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
	}

	public int getLoginType() {
		return loginType;
	}

	public boolean getNeedPay() {
		return needPay;
	}

	public void setNeedPay(boolean needPay) {
		this.needPay = needPay;
	}

	public boolean getCanGame() {
		return canGame;
	}

	public void setCanGame(boolean canGame) {
		this.canGame = canGame;
	}

    public boolean findPaiyangFlag() {
		boolean flag = false;

		if(inno72MachineVo!=null && inno72MachineVo.getActivityType() == Inno72MachineVo.ACTIVITYTYPE_PAIYANG){
			flag = true;
		}

		return flag;
    }
	public boolean getIsScanned() {
		return isScanned;
	}

	public void setIsScanned(boolean isScanned) {
		this.isScanned = isScanned;
	}

}
