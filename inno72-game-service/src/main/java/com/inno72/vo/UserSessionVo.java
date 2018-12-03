package com.inno72.vo;

import java.util.List;

import com.inno72.common.SessionConstants;
import com.inno72.sessionshare.utils.SessionUtils;
import com.sun.org.apache.xpath.internal.operations.Bool;
import lombok.Data;

import javax.servlet.http.HttpSession;

public class UserSessionVo {
	private HttpSession httpSession;
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
//	private Integer displayNewRetailMemberUrlFlag;
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

	public UserSessionVo(String machineCode ,String mid, String userNick, String userId, String access_token, String gameId,
			String sessionUuid, String planId) {
		super();
		this.machineCode = machineCode;
		this.mid = mid;
		this.userNick = userNick;
		this.userId = userId;
		this.accessToken = access_token;
		this.gameId = gameId;
		this.sessionUuid = sessionUuid;
		this.activityPlanId = planId;
		httpSession = SessionUtils.getSession(machineCode,true);
		httpSession.setAttribute(SessionConstants.MACHINECODE,machineCode);
		httpSession.setAttribute(SessionConstants.MID,mid);
		httpSession.setAttribute(SessionConstants.USERNICK,userNick);
		httpSession.setAttribute(SessionConstants.USERID,userId);
		httpSession.setAttribute(SessionConstants.ACCESSTOKEN,accessToken);
		httpSession.setAttribute(SessionConstants.GAMEID,gameId);
		httpSession.setAttribute(SessionConstants.SESSIONUUID,sessionUuid);
		httpSession.setAttribute(SessionConstants.ACTIVITYPLANID,activityPlanId);
	}

	public UserSessionVo(String machineCode) {
		super();
		httpSession = SessionUtils.getSession(machineCode,true);
	}


	public void setCanOrder(boolean canOrder) {
		this.canOrder = canOrder;
		httpSession.setAttribute(SessionConstants.CANORDER,canOrder);
	}

	public boolean getCanOrder() {
		return (Boolean) httpSession.getAttribute(SessionConstants.CANORDER);
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
		httpSession.setAttribute(SessionConstants.CHANNELID,channelId);
	}

	public String getChannelId() {
		return (String)httpSession.getAttribute(SessionConstants.CHANNELID);
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
		httpSession.setAttribute(SessionConstants.MACHINEID,machineId);
	}

	public String getMachineId() {
		return (String)httpSession.getAttribute(SessionConstants.MACHINEID);
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
		httpSession.setAttribute(SessionConstants.ACTIVITYID,activityId);
	}

	public String getActivityId() {
		return (String)httpSession.getAttribute(SessionConstants.ACTIVITYID);
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
		httpSession.setAttribute(SessionConstants.MACHINECODE,machineCode);
	}

	public String getMachineCode() {
		return (String)httpSession.getAttribute(SessionConstants.MACHINECODE);
	}

	public void setCountGoods(boolean countGoods) {
		this.countGoods = countGoods;
		httpSession.setAttribute(SessionConstants.COUNTGOODS,countGoods);
	}

	public boolean getCountGoods() {
		return (Boolean)httpSession.getAttribute(SessionConstants.COUNTGOODS);
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
		httpSession.setAttribute(SessionConstants.LOGINTYPE,loginType);
	}

	public int getLoginType() {
		return (Integer)httpSession.getAttribute(SessionConstants.LOGINTYPE);
	}

	public boolean getNeedPay() {
		return (Boolean)httpSession.getAttribute(SessionConstants.NEEDPAY);
	}

	public void setNeedPay(boolean needPay) {
		this.needPay = needPay;
		httpSession.setAttribute(SessionConstants.NEEDPAY,needPay);
	}

	public boolean getCanGame() {
		return (Boolean)httpSession.getAttribute(SessionConstants.CANGAME);
	}

	public void setCanGame(boolean canGame) {
		this.canGame = canGame;
		httpSession.setAttribute(SessionConstants.CANGAME,canGame);
	}

	public boolean getIsScanned() {
		return (Boolean)httpSession.getAttribute(SessionConstants.ISSCANNED);
	}

	public void setIsScanned(boolean isScanned) {
		this.isScanned = isScanned;
		httpSession.setAttribute(SessionConstants.ISSCANNED,isScanned);
	}

	public String getTraceId() {
		return (String)httpSession.getAttribute(SessionConstants.TRACEID);
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
		httpSession.setAttribute(SessionConstants.TRACEID,traceId);
	}

	public String getPlanCode() {
		return (String)httpSession.getAttribute(SessionConstants.PLANCODE);
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
		httpSession.setAttribute(SessionConstants.PLANCODE,planCode);
	}

	public String getSellerName() {
		return (String)httpSession.getAttribute(SessionConstants.SELLERNAME);
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
		httpSession.setAttribute(SessionConstants.SELLERNAME,sellerName);
	}

	public String getShipmentNum() {
		return (String)httpSession.getAttribute(SessionConstants.SHIPMENTNUM);
	}

	public void setShipmentNum(String shipmentNum) {
		this.shipmentNum = shipmentNum;
		httpSession.setAttribute(SessionConstants.SHIPMENTNUM,shipmentNum);
	}

	public String getScanUrl() {
		return (String)httpSession.getAttribute(SessionConstants.SCANURL);
	}

	public void setScanUrl(String scanUrl) {
		this.scanUrl = scanUrl;
		httpSession.setAttribute(SessionConstants.SCANURL,scanUrl);
	}

	public String getGoodsName() {
		return (String)httpSession.getAttribute(SessionConstants.GOODSNAME);
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
		httpSession.setAttribute(SessionConstants.GOODSNAME,goodsName);
	}

	public String getRefOrderStatus() {
		return (String)httpSession.getAttribute(SessionConstants.REFORDERSTATUS);
	}

	public void setRefOrderStatus(String refOrderStatus) {
		this.refOrderStatus = refOrderStatus;
		httpSession.setAttribute(SessionConstants.REFORDERSTATUS,refOrderStatus);
	}

	public String getScanLoginUrl() {
		return (String)httpSession.getAttribute(SessionConstants.SCANLOGINURL);
	}

	public void setScanLoginUrl(String scanLoginUrl) {
		this.scanLoginUrl = scanLoginUrl;
		httpSession.setAttribute(SessionConstants.SCANLOGINURL,scanLoginUrl);
	}

	public String getScanPayUrl() {
		return (String)httpSession.getAttribute(SessionConstants.SCANPAYURL);
	}

	public void setScanPayUrl(String scanPayUrl) {
		this.scanPayUrl = scanPayUrl;
		httpSession.setAttribute(SessionConstants.SCANPAYURL,scanPayUrl);
	}

	public String getInteractId() {
		return (String)httpSession.getAttribute(SessionConstants.INTERACTID);
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
		httpSession.setAttribute(SessionConstants.INTERACTID,interactId);
	}

	public String getGameUserId() {
		return  (String)httpSession.getAttribute(SessionConstants.GAMEUSERID);
	}

	public void setGameUserId(String gameUserId) {
		this.gameUserId = gameUserId;
		httpSession.setAttribute(SessionConstants.GAMEUSERID,gameUserId);
	}

	public String getMid() {
		return (String)httpSession.getAttribute(SessionConstants.MID);
	}

	public void setMid(String mid) {
		this.mid = mid;
		httpSession.setAttribute(SessionConstants.MID,mid);
	}

	public String getUserNick() {
		return (String)httpSession.getAttribute(SessionConstants.USERNICK);
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
		httpSession.setAttribute(SessionConstants.USERNICK,userNick);
	}

	public String getUserId() {
		return (String)httpSession.getAttribute(SessionConstants.USERID);
	}

	public void setUserId(String userId) {
		this.userId = userId;
		httpSession.setAttribute(SessionConstants.USERID,userId);
	}

	public String getAccessToken() {
		return (String)httpSession.getAttribute(SessionConstants.ACCESSTOKEN);
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		httpSession.setAttribute(SessionConstants.ACCESSTOKEN,accessToken);
	}

	public String getGameId() {
		return (String)httpSession.getAttribute(SessionConstants.GAMEID);
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
		httpSession.setAttribute(SessionConstants.GAMEID,gameId);
	}

	public String getSessionUuid() {
		return (String)httpSession.getAttribute(SessionConstants.SESSIONUUID);
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
		httpSession.setAttribute(SessionConstants.SESSIONUUID,sessionUuid);
	}

	public String getMixnick() {
		return (String)httpSession.getAttribute(SessionConstants.MIXNICK);
	}

	public void setMixnick(String mixnick) {
		this.mixnick = mixnick;
		httpSession.setAttribute(SessionConstants.MIXNICK,mixnick);
	}

	public String getSource() {
		return (String)httpSession.getAttribute(SessionConstants.SOURCE);
	}

	public void setSource(String source) {
		this.source = source;
		httpSession.setAttribute(SessionConstants.SOURCE,source);
	}

	public String getRefOrderId() {
		return (String)httpSession.getAttribute(SessionConstants.REFORDERID);
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
		httpSession.setAttribute(SessionConstants.REFORDERID,refOrderId);
	}

	public String getSellerId() {
		return (String)httpSession.getAttribute(SessionConstants.SELLERID);
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
		httpSession.setAttribute(SessionConstants.SELLERID,sellerId);
	}

	public String getMerchantName() {
		return (String)httpSession.getAttribute(SessionConstants.MERCHANTNAME);
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
		httpSession.setAttribute(SessionConstants.MERCHANTNAME,merchantName);
	}

	public Inno72MachineVo getInno72MachineVo() {
		return (Inno72MachineVo)httpSession.getAttribute(SessionConstants.INNO72MACHINEVO);
	}

	public void setInno72MachineVo(Inno72MachineVo inno72MachineVo) {
		this.inno72MachineVo = inno72MachineVo;
		httpSession.setAttribute(SessionConstants.INNO72MACHINEVO,inno72MachineVo);
	}

	public String getMerchantAccountId() {
		return (String)httpSession.getAttribute(SessionConstants.MERCHANTACCOUNTID);
	}

	public void setMerchantAccountId(String merchantAccountId) {
		this.merchantAccountId = merchantAccountId;
		httpSession.setAttribute(SessionConstants.MERCHANTACCOUNTID,merchantAccountId);
	}

	public String getMerchantAccountName() {
		return (String)httpSession.getAttribute(SessionConstants.MERCHANTACCOUNTNAME);
	}

	public void setMerchantAccountName(String merchantAccountName) {
		this.merchantAccountName = merchantAccountName;
		httpSession.setAttribute(SessionConstants.MERCHANTACCOUNTNAME,merchantAccountName);
	}

	public String getChannelMerchantId() {
		return  (String)httpSession.getAttribute(SessionConstants.CHANNELMERCHANTID);
	}

	public void setChannelMerchantId(String channelMerchantId) {
		this.channelMerchantId = channelMerchantId;
		httpSession.setAttribute(SessionConstants.CHANNELMERCHANTID,channelMerchantId);
	}

	public String getChannelName() {
		return  (String)httpSession.getAttribute(SessionConstants.CHANNELNAME);
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
		httpSession.setAttribute(SessionConstants.CHANNELNAME,channelName);
	}

	public String getGameReport() {
		return  (String)httpSession.getAttribute(SessionConstants.GAMEREPORT);
	}

	public void setGameReport(String gameReport) {
		this.gameReport = gameReport;
		httpSession.setAttribute(SessionConstants.GAMEREPORT,gameReport);
	}

	public String getInno72OrderId() {
		return  (String)httpSession.getAttribute(SessionConstants.INNO72ORDERID);
	}

	public void setInno72OrderId(String inno72OrderId) {
		this.inno72OrderId = inno72OrderId;
		httpSession.setAttribute(SessionConstants.INNO72ORDERID,inno72OrderId);
	}

	public String getInno72CouponOrderId() {
		return  (String)httpSession.getAttribute(SessionConstants.INNO72COUPONORDERID);
	}

	public void setInno72CouponOrderId(String inno72CouponOrderId) {
		this.inno72CouponOrderId = inno72CouponOrderId;
		httpSession.setAttribute(SessionConstants.INNO72COUPONORDERID,inno72CouponOrderId);
	}

	public String getActivityPlanId() {
		return  (String)httpSession.getAttribute(SessionConstants.ACTIVITYPLANID);
	}

	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
		httpSession.setAttribute(SessionConstants.ACTIVITYPLANID,activityPlanId);
	}

	public Long getPlayTimes() {
		return  (Long)httpSession.getAttribute(SessionConstants.PLAYTIMES);
	}

	public void setPlayTimes(Long playTimes) {
		this.playTimes = playTimes;
		httpSession.setAttribute(SessionConstants.PLAYTIMES,playTimes);
	}

	public boolean isCanOrder() {
		return (Boolean)httpSession.getAttribute(SessionConstants.CANORDER);
	}

	public boolean isCanGame() {
		return (Boolean)httpSession.getAttribute(SessionConstants.CANGAME);
	}

	public Integer getChannelType() {
		return (Integer)httpSession.getAttribute(SessionConstants.CHANNELTYPE);
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
		httpSession.setAttribute(SessionConstants.CHANNELTYPE,channelType);
	}

	public boolean isCountGoods() {
		return (Boolean)httpSession.getAttribute(SessionConstants.COUNTGOODS);
	}

	public List<GoodsVo> getGoodsList() {
		return (List<GoodsVo>)httpSession.getAttribute(SessionConstants.GOODSLIST);
	}

	public void setGoodsList(List<GoodsVo> goodsList) {
		this.goodsList = goodsList;
		httpSession.setAttribute(SessionConstants.GOODSLIST,goodsList);
	}

	public boolean isLogged() {
		return (Boolean)httpSession.getAttribute(SessionConstants.LOGGED);
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
		httpSession.setAttribute(SessionConstants.LOGGED,logged);
	}

	public String getGoodsId() {
		return (String)httpSession.getAttribute(SessionConstants.GOODSID);
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
		httpSession.setAttribute(SessionConstants.GOODSID,goodsId);
	}

	public String getGoodsCode() {
		return (String)httpSession.getAttribute(SessionConstants.GOODSCODE);
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
		httpSession.setAttribute(SessionConstants.GOODSCODE,goodsCode);
	}

	public String getAuthUrl() {
		return (String)httpSession.getAttribute(SessionConstants.AUTHURL);
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
		httpSession.setAttribute(SessionConstants.AUTHURL,authUrl);
	}

	public boolean isNeedPay() {
		return (Boolean)httpSession.getAttribute(SessionConstants.NEEDPAY);
	}

	public String getIsVip() {
		return (String)httpSession.getAttribute(SessionConstants.ISVIP);
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
		httpSession.setAttribute(SessionConstants.ISVIP,isVip);
	}

	public String getSessionKey() {
		return (String)httpSession.getAttribute(SessionConstants.SESSIONKEY);
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		httpSession.setAttribute(SessionConstants.SESSIONKEY,sessionKey);
	}

	public boolean isScanned() {
		return (Boolean)httpSession.getAttribute(SessionConstants.ISSCANNED);
	}

	public void setScanned(boolean scanned) {
		isScanned = scanned;
		httpSession.setAttribute(SessionConstants.ISSCANNED,isScanned);
	}

	public String getNewRetailMemberUrl() {
		return (String)httpSession.getAttribute(SessionConstants.NEWRETAILMEMBERURL);
	}

	public void setNewRetailMemberUrl(String newRetailMemberUrl) {
		this.newRetailMemberUrl = newRetailMemberUrl;
		httpSession.setAttribute(SessionConstants.NEWRETAILMEMBERURL,newRetailMemberUrl);
	}

	public Integer getGoodsType() {
		return (Integer)httpSession.getAttribute(SessionConstants.GOODSTYPE);
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
		httpSession.setAttribute(SessionConstants.GOODSTYPE,goodsType);
	}

	public Integer getActivityType() {
		return (Integer)httpSession.getAttribute(SessionConstants.ACTIVITYTYPE);
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
		httpSession.setAttribute(SessionConstants.ACTIVITYTYPE,activityType);
	}

	public boolean isFllowed() {
		return (Boolean)httpSession.getAttribute(SessionConstants.FLLOWED);
	}

	public void setFllowed(boolean fllowed) {
		this.fllowed = fllowed;
		httpSession.setAttribute(SessionConstants.FLLOWED,fllowed);
	}

	public boolean findPaiyangFlag() {
		boolean flag = false;

		if(inno72MachineVo!=null && inno72MachineVo.getActivityType() == Inno72MachineVo.ACTIVITYTYPE_PAIYANG){
			flag = true;
		}

		return flag;
	}
}
