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

	public Boolean getCanOrder() {
		Object obj = httpSession.getAttribute(SessionConstants.CANORDER);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setChannelId(String channelId) {
		this.channelId = channelId;
		httpSession.setAttribute(SessionConstants.CHANNELID,channelId);
	}

	public String getChannelId() {
		Object obj = httpSession.getAttribute(SessionConstants.CHANNELID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMachineId(String machineId) {
		this.machineId = machineId;
		httpSession.setAttribute(SessionConstants.MACHINEID,machineId);
	}

	public String getMachineId() {
		Object obj = httpSession.getAttribute(SessionConstants.MACHINEID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setActivityId(String activityId) {
		this.activityId = activityId;
		httpSession.setAttribute(SessionConstants.ACTIVITYID,activityId);
	}

	public String getActivityId() {
		Object obj = httpSession.getAttribute(SessionConstants.ACTIVITYID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMachineCode(String machineCode) {
		this.machineCode = machineCode;
		httpSession.setAttribute(SessionConstants.MACHINECODE,machineCode);
	}

	public String getMachineCode() {
		Object obj = httpSession.getAttribute(SessionConstants.MACHINECODE);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setCountGoods(boolean countGoods) {
		this.countGoods = countGoods;
		httpSession.setAttribute(SessionConstants.COUNTGOODS,countGoods);
	}

	public Boolean getCountGoods() {
		Object obj = httpSession.getAttribute(SessionConstants.COUNTGOODS);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setLoginType(int loginType) {
		this.loginType = loginType;
		httpSession.setAttribute(SessionConstants.LOGINTYPE,loginType);
	}

	public Integer getLoginType() {
		Object obj = httpSession.getAttribute(SessionConstants.LOGINTYPE);
		if(obj == null) return null;
		return (Integer)obj ;
	}

	public Boolean getNeedPay() {
		Object obj = httpSession.getAttribute(SessionConstants.NEEDPAY);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setNeedPay(boolean needPay) {
		this.needPay = needPay;
		httpSession.setAttribute(SessionConstants.NEEDPAY,needPay);
	}

	public Boolean getCanGame() {
		Object obj = httpSession.getAttribute(SessionConstants.CANGAME);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setCanGame(boolean canGame) {
		this.canGame = canGame;
		httpSession.setAttribute(SessionConstants.CANGAME,canGame);
	}

	public Boolean getIsScanned() {
		Object obj = httpSession.getAttribute(SessionConstants.ISSCANNED);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setIsScanned(boolean isScanned) {
		this.isScanned = isScanned;
		httpSession.setAttribute(SessionConstants.ISSCANNED,isScanned);
	}

	public String getTraceId() {
		Object obj = httpSession.getAttribute(SessionConstants.TRACEID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setTraceId(String traceId) {
		this.traceId = traceId;
		httpSession.setAttribute(SessionConstants.TRACEID,traceId);
	}

	public String getPlanCode() {
		Object obj = httpSession.getAttribute(SessionConstants.PLANCODE);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setPlanCode(String planCode) {
		this.planCode = planCode;
		httpSession.setAttribute(SessionConstants.PLANCODE,planCode);
	}

	public String getSellerName() {
		Object obj = httpSession.getAttribute(SessionConstants.SELLERNAME);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setSellerName(String sellerName) {
		this.sellerName = sellerName;
		httpSession.setAttribute(SessionConstants.SELLERNAME,sellerName);
	}

	public String getShipmentNum() {
		Object obj = httpSession.getAttribute(SessionConstants.SHIPMENTNUM);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setShipmentNum(String shipmentNum) {
		this.shipmentNum = shipmentNum;
		httpSession.setAttribute(SessionConstants.SHIPMENTNUM,shipmentNum);
	}

	public String getScanUrl() {
		Object obj = httpSession.getAttribute(SessionConstants.SCANURL);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setScanUrl(String scanUrl) {
		this.scanUrl = scanUrl;
		httpSession.setAttribute(SessionConstants.SCANURL,scanUrl);
	}

	public String getGoodsName() {
		Object obj = httpSession.getAttribute(SessionConstants.GOODSNAME);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
		httpSession.setAttribute(SessionConstants.GOODSNAME,goodsName);
	}

	public String getRefOrderStatus() {
		Object obj = httpSession.getAttribute(SessionConstants.REFORDERSTATUS);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setRefOrderStatus(String refOrderStatus) {
		this.refOrderStatus = refOrderStatus;
		httpSession.setAttribute(SessionConstants.REFORDERSTATUS,refOrderStatus);
	}

	public String getScanLoginUrl() {
		Object obj = httpSession.getAttribute(SessionConstants.SCANLOGINURL);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setScanLoginUrl(String scanLoginUrl) {
		this.scanLoginUrl = scanLoginUrl;
		httpSession.setAttribute(SessionConstants.SCANLOGINURL,scanLoginUrl);
	}

	public String getScanPayUrl() {
		Object obj = httpSession.getAttribute(SessionConstants.SCANPAYURL);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setScanPayUrl(String scanPayUrl) {
		this.scanPayUrl = scanPayUrl;
		httpSession.setAttribute(SessionConstants.SCANPAYURL,scanPayUrl);
	}

	public String getInteractId() {
		Object obj = httpSession.getAttribute(SessionConstants.INTERACTID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setInteractId(String interactId) {
		this.interactId = interactId;
		httpSession.setAttribute(SessionConstants.INTERACTID,interactId);
	}

	public String getGameUserId() {
		Object obj = httpSession.getAttribute(SessionConstants.GAMEUSERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGameUserId(String gameUserId) {
		this.gameUserId = gameUserId;
		httpSession.setAttribute(SessionConstants.GAMEUSERID,gameUserId);
	}

	public String getMid() {
		Object obj = httpSession.getAttribute(SessionConstants.MID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMid(String mid) {
		this.mid = mid;
		httpSession.setAttribute(SessionConstants.MID,mid);
	}

	public String getUserNick() {
		Object obj = httpSession.getAttribute(SessionConstants.USERNICK);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setUserNick(String userNick) {
		this.userNick = userNick;
		httpSession.setAttribute(SessionConstants.USERNICK,userNick);
	}

	public String getUserId() {
		Object obj = httpSession.getAttribute(SessionConstants.USERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setUserId(String userId) {
		this.userId = userId;
		httpSession.setAttribute(SessionConstants.USERID,userId);
	}

	public String getAccessToken() {
		Object obj = httpSession.getAttribute(SessionConstants.ACCESSTOKEN);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
		httpSession.setAttribute(SessionConstants.ACCESSTOKEN,accessToken);
	}

	public String getGameId() {
		Object obj = httpSession.getAttribute(SessionConstants.GAMEID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGameId(String gameId) {
		this.gameId = gameId;
		httpSession.setAttribute(SessionConstants.GAMEID,gameId);
	}

	public String getSessionUuid() {
		Object obj = httpSession.getAttribute(SessionConstants.SESSIONUUID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setSessionUuid(String sessionUuid) {
		this.sessionUuid = sessionUuid;
		httpSession.setAttribute(SessionConstants.SESSIONUUID,sessionUuid);
	}

	public String getMixnick() {
		Object obj = httpSession.getAttribute(SessionConstants.MIXNICK);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMixnick(String mixnick) {
		this.mixnick = mixnick;
		httpSession.setAttribute(SessionConstants.MIXNICK,mixnick);
	}

	public String getSource() {
		Object obj = httpSession.getAttribute(SessionConstants.SOURCE);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setSource(String source) {
		this.source = source;
		httpSession.setAttribute(SessionConstants.SOURCE,source);
	}

	public String getRefOrderId() {
		Object obj = httpSession.getAttribute(SessionConstants.REFORDERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setRefOrderId(String refOrderId) {
		this.refOrderId = refOrderId;
		httpSession.setAttribute(SessionConstants.REFORDERID,refOrderId);
	}

	public String getSellerId() {
		Object obj = httpSession.getAttribute(SessionConstants.SELLERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setSellerId(String sellerId) {
		this.sellerId = sellerId;
		httpSession.setAttribute(SessionConstants.SELLERID,sellerId);
	}

	public String getMerchantName() {
		Object obj = httpSession.getAttribute(SessionConstants.MERCHANTNAME);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMerchantName(String merchantName) {
		this.merchantName = merchantName;
		httpSession.setAttribute(SessionConstants.MERCHANTNAME,merchantName);
	}

	public Inno72MachineVo getInno72MachineVo() {
		Object obj = httpSession.getAttribute(SessionConstants.INNO72MACHINEVO);
		if(obj == null) return null;
		return (Inno72MachineVo)obj ;
	}

	public void setInno72MachineVo(Inno72MachineVo inno72MachineVo) {
		this.inno72MachineVo = inno72MachineVo;
		httpSession.setAttribute(SessionConstants.INNO72MACHINEVO,inno72MachineVo);
	}

	public String getMerchantAccountId() {
		Object obj = httpSession.getAttribute(SessionConstants.MERCHANTACCOUNTID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMerchantAccountId(String merchantAccountId) {
		this.merchantAccountId = merchantAccountId;
		httpSession.setAttribute(SessionConstants.MERCHANTACCOUNTID,merchantAccountId);
	}

	public String getMerchantAccountName() {
		Object obj = httpSession.getAttribute(SessionConstants.MERCHANTACCOUNTNAME);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setMerchantAccountName(String merchantAccountName) {
		this.merchantAccountName = merchantAccountName;
		httpSession.setAttribute(SessionConstants.MERCHANTACCOUNTNAME,merchantAccountName);
	}

	public String getChannelMerchantId() {
		Object obj = httpSession.getAttribute(SessionConstants.CHANNELMERCHANTID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setChannelMerchantId(String channelMerchantId) {
		this.channelMerchantId = channelMerchantId;
		httpSession.setAttribute(SessionConstants.CHANNELMERCHANTID,channelMerchantId);
	}

	public String getChannelName() {
		Object obj = httpSession.getAttribute(SessionConstants.CHANNELNAME);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setChannelName(String channelName) {
		this.channelName = channelName;
		httpSession.setAttribute(SessionConstants.CHANNELNAME,channelName);
	}

	public String getGameReport() {
		Object obj = httpSession.getAttribute(SessionConstants.GAMEREPORT);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGameReport(String gameReport) {
		this.gameReport = gameReport;
		httpSession.setAttribute(SessionConstants.GAMEREPORT,gameReport);
	}

	public String getInno72OrderId() {
		Object obj = httpSession.getAttribute(SessionConstants.INNO72ORDERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setInno72OrderId(String inno72OrderId) {
		this.inno72OrderId = inno72OrderId;
		httpSession.setAttribute(SessionConstants.INNO72ORDERID,inno72OrderId);
	}

	public String getInno72CouponOrderId() {
		Object obj = httpSession.getAttribute(SessionConstants.INNO72COUPONORDERID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setInno72CouponOrderId(String inno72CouponOrderId) {
		this.inno72CouponOrderId = inno72CouponOrderId;
		httpSession.setAttribute(SessionConstants.INNO72COUPONORDERID,inno72CouponOrderId);
	}

	public String getActivityPlanId() {
		Object obj = httpSession.getAttribute(SessionConstants.ACTIVITYPLANID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setActivityPlanId(String activityPlanId) {
		this.activityPlanId = activityPlanId;
		httpSession.setAttribute(SessionConstants.ACTIVITYPLANID,activityPlanId);
	}

	public Long getPlayTimes() {
		Object obj = httpSession.getAttribute(SessionConstants.PLAYTIMES);
		if(obj == null) return null;
		return (Long)obj ;
	}

	public void setPlayTimes(Long playTimes) {
		this.playTimes = playTimes;
		httpSession.setAttribute(SessionConstants.PLAYTIMES,playTimes);
	}

	public Boolean isCanOrder() {
		Object obj = httpSession.getAttribute(SessionConstants.CANORDER);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public Boolean isCanGame() {
		Object obj = httpSession.getAttribute(SessionConstants.CANGAME);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public Integer getChannelType() {
		Object obj = httpSession.getAttribute(SessionConstants.CHANNELTYPE);
		if(obj == null) return null;
		return (Integer)obj ;
	}

	public void setChannelType(Integer channelType) {
		this.channelType = channelType;
		httpSession.setAttribute(SessionConstants.CHANNELTYPE,channelType);
	}

	public Boolean isCountGoods() {
		Object obj = httpSession.getAttribute(SessionConstants.COUNTGOODS);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public List<GoodsVo> getGoodsList() {
		Object obj = httpSession.getAttribute(SessionConstants.GOODSLIST);
		if(obj == null) return null;
		return (List<GoodsVo>)obj ;
	}

	public void setGoodsList(List<GoodsVo> goodsList) {
		this.goodsList = goodsList;
		httpSession.setAttribute(SessionConstants.GOODSLIST,goodsList);
	}

	public Boolean isLogged() {
		Object obj = httpSession.getAttribute(SessionConstants.LOGGED);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setLogged(boolean logged) {
		this.logged = logged;
		httpSession.setAttribute(SessionConstants.LOGGED,logged);
	}

	public String getGoodsId() {
		Object obj = httpSession.getAttribute(SessionConstants.GOODSID);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
		httpSession.setAttribute(SessionConstants.GOODSID,goodsId);
	}

	public String getGoodsCode() {
		Object obj = httpSession.getAttribute(SessionConstants.GOODSCODE);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
		httpSession.setAttribute(SessionConstants.GOODSCODE,goodsCode);
	}

	public String getAuthUrl() {
		Object obj = httpSession.getAttribute(SessionConstants.AUTHURL);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setAuthUrl(String authUrl) {
		this.authUrl = authUrl;
		httpSession.setAttribute(SessionConstants.AUTHURL,authUrl);
	}

	public Boolean isNeedPay() {
		Object obj = httpSession.getAttribute(SessionConstants.NEEDPAY);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public String getIsVip() {
		Object obj = httpSession.getAttribute(SessionConstants.ISVIP);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setIsVip(String isVip) {
		this.isVip = isVip;
		httpSession.setAttribute(SessionConstants.ISVIP,isVip);
	}

	public String getSessionKey() {
		Object obj = httpSession.getAttribute(SessionConstants.SESSIONKEY);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setSessionKey(String sessionKey) {
		this.sessionKey = sessionKey;
		httpSession.setAttribute(SessionConstants.SESSIONKEY,sessionKey);
	}

	public Boolean isScanned() {
		Object obj = httpSession.getAttribute(SessionConstants.ISSCANNED);
		if(obj == null) return null;
		return (Boolean)obj ;
	}

	public void setScanned(boolean scanned) {
		isScanned = scanned;
		httpSession.setAttribute(SessionConstants.ISSCANNED,isScanned);
	}

	public String getNewRetailMemberUrl() {
		Object obj = httpSession.getAttribute(SessionConstants.NEWRETAILMEMBERURL);
		if(obj == null) return null;
		return (String)obj ;
	}

	public void setNewRetailMemberUrl(String newRetailMemberUrl) {
		this.newRetailMemberUrl = newRetailMemberUrl;
		httpSession.setAttribute(SessionConstants.NEWRETAILMEMBERURL,newRetailMemberUrl);
	}

	public Integer getGoodsType() {
		Object obj = httpSession.getAttribute(SessionConstants.GOODSTYPE);
		if(obj == null) return null;
		return (Integer)obj ;
	}

	public void setGoodsType(Integer goodsType) {
		this.goodsType = goodsType;
		httpSession.setAttribute(SessionConstants.GOODSTYPE,goodsType);
	}

	public Integer getActivityType() {
		Object obj = httpSession.getAttribute(SessionConstants.ACTIVITYTYPE);
		if(obj == null) return null;
		return (Integer)obj ;
	}

	public void setActivityType(Integer activityType) {
		this.activityType = activityType;
		httpSession.setAttribute(SessionConstants.ACTIVITYTYPE,activityType);
	}

	public Boolean isFllowed() {
		Object obj = httpSession.getAttribute(SessionConstants.FLLOWED);
		if(obj == null) return null;
		return (Boolean)obj ;
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
