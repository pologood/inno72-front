package com.inno72.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.inno72.common.CommonBean;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.AesUtils;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.mapper.Inno72CouponMapper;
import com.inno72.model.Inno72Coupon;
import com.inno72.model.Inno72MachineConnectionMsg;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.*;
import com.alibaba.fastjson.JSON;
import com.inno72.common.util.UuidUtil;
import com.inno72.util.StringUtil;
import com.inno72.vo.*;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Inno72GameServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.TopH5ErrorTypeEnum;
import com.inno72.common.datetime.LocalDateTimeUtil;
import com.inno72.common.util.GameSessionRedisUtil;
import com.inno72.log.PointLogContext;
import com.inno72.log.vo.LogType;
import tk.mybatis.mapper.entity.Condition;

/**
 * 标准接口
 */
@RestController
@RequestMapping(value = "/api/standard")
public class Inno72StandardController {

	private Logger logger = LoggerFactory.getLogger(this.getClass());


	@Resource
	private Inno72AuthInfoService inno72AuthInfoService;
	@Resource
	private Inno72ConnectionService inno72ConnectionService;

	@Resource
	private Inno72GameApiService inno72GameApiService;

	@Resource
	private Inno72MachineService inno72MachineService;

	@Resource
	private GameSessionRedisUtil gameSessionRedisUtil;

	@Resource
	private IRedisUtil iRedisUtil;

	@Resource
	private Inno72CouponMapper inno72CouponMapper;

	@Resource
	private Inno72TopService inno72TopService;

	@Resource
	private Inno72GameServiceProperties inno72GameServiceProperties;

	@Resource
	private PointService pointService;

	@Value("${top_h5_err_url}")
	private String topH5ErrUrl;

	@Value("${env}")
	private String env;

	private final static String REPEAT_INVOKE_KEY ="REPEAT_INVOKE:";

	/**
	 * 登录（包括需要登录和非登录的场景）
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/prepareLogin", method = {RequestMethod.POST})
	public Result<Object> prepareLogin(StandardPrepareLoginReqVo req) {
		if (StringUtils.isBlank(req.getMachineCode())) {
			logger.warn("机器Code为空 " + req.toString());
			return Results.failure("机器Code为空");
		}

		if (!StandardLoginTypeEnum.isExist(req.getLoginType())) {
			logger.warn("登陆类型错误 " + req.toString());
			return Results.failure("登陆类型错误");
		}

		if (StandardLoginTypeEnum.NOLOGIN.getValue().equals(req.getLoginType())) {
			return inno72GameApiService.prepareLoginNologin(req.getMachineCode());
		} else {
			return inno72GameApiService.prepareLoginQrCode(req);
		}
	}

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72StandardController.class);

	/**
	 * 测试埋点接口
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/logger", method = {RequestMethod.POST})
	public void logger(StandardPrepareLoginReqVo req) {
		new PointLogContext(LogType.POINT).machineCode("ceshimachinecode123")
				.pointTime(LocalDateTimeUtil.transfer(LocalDateTime.now(),
						DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")))
				.type("31").detail("测试").tag("测试tag").bulid();
		LOGGER.info("记录埋点数据 [测试]");
	}

	/**
	 * 下单（包括订单及优惠券）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/order", method = {RequestMethod.POST})
	public Result<Object> order(MachineApiVo vo) {

		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("会话不存在!" + vo.toString());
		}

		//处理重复下单
		boolean flag =  managRepeatOrder(userSessionVo.getMachineCode(),userSessionVo.getUserId());
		if(!flag){
			return Results.failure("重复下单");
		}
		return inno72GameApiService.standardOrder(vo);
	}

	private boolean managRepeatOrder(String machineCode, String userId) {
		String key = REPEAT_INVOKE_KEY + machineCode +":"+userId;
		if(iRedisUtil.exists(key)){
			return false;
		}
		synchronized (Inno72StandardController.class){
			if(iRedisUtil.exists(key)){
				return false;
			}else{
				iRedisUtil.setex(key,10,"1");
			}
		}
		return true;
	}

	/**
	 * 发券（单独发优惠券流程）
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/lottery", method = {RequestMethod.POST})
	public Result<Object> lottery(MachineApiVo vo) {
		UserSessionVo userSessionVo = gameSessionRedisUtil.getSessionKey(vo.getSessionUuid());
		if (userSessionVo == null) {
			return Results.failure("会话不存在!" + vo.toString());
		}

		Condition condition = new Condition(Inno72Coupon.class);
		condition.createCriteria().andEqualTo("code", vo.getInteractId());
		List<Inno72Coupon> inno72Coupons = inno72CouponMapper.selectByCondition(condition);
		if (CollectionUtils.isEmpty(inno72Coupons)) {
			return Results.failure(vo.getInteractId() + "不存在");
		}
		String prizeId = inno72Coupons.get(0).getId();
		logger.info("lottery prizeId is {}", prizeId);
		return inno72GameApiService.lottery(userSessionVo, vo.getUa(), vo.getUmid(), prizeId);
	}

	/**
	 * 出货（包括正常异常流程）
	 * @param req
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/shipment", method = {RequestMethod.POST})
	public Result<String> shipment(StandardShipmentReqVo req) {

		MachineApiVo vo = new MachineApiVo();
		vo.setMachineId(req.getMachineCode());
		vo.setChannelId(req.getChannelId());
		vo.setFailChannelIds(req.getFailChannelIds());
		vo.setSessionUuid(req.getSessionUuid());
		return inno72GameApiService.shipmentReportV2(vo);

	}

	/**
	 * 获得活动信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findActivity", method = {RequestMethod.POST})
	public Result findActivity(@RequestParam(name = "machineId") String mid, String planId, String version,
							   String versionInno72, HttpSession session) {
		return inno72MachineService.findGame(mid, planId, version, versionInno72);
	}
	/**
	 * 获得活动信息
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/findActivityForApp", method = {RequestMethod.POST})
	public Result findActivityForApp(@RequestBody Map<String,Object> map) {

		String data = map.get("data").toString();
		String decryptData = AesUtils.decrypt(data);
		String result = null;
		try {
			result =  new String(decryptData.getBytes("UTF-8"),"UTF-8");
			String mid = FastJsonUtils.getString(result, "machineId");
			Long _t = Long.parseLong(FastJsonUtils.getString(result, "_t"));

			return inno72MachineService.findActivityForApp(mid, _t);
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * polling 用户登录信息
	 * @param sessionUuid
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/sessionPolling", method = {RequestMethod.POST})
	public Result<Object> sessionPolling(String sessionUuid) {
		return inno72AuthInfoService.sessionPolling(sessionUuid);
	}

	/**
	 * polling 订单支付状态
	 * @param vo
	 * @return
	 */
	@ResponseBody
	@RequestMapping(value = "/orderPolling", method = {RequestMethod.POST})
	public Result<Object> orderPolling(MachineApiVo vo) {
		return inno72GameApiService.orderPolling(vo);
	}

	/**
	 * 设置用户已登录
	 */
	@ResponseBody
	@RequestMapping(value = "/setLogged", method = {RequestMethod.POST})
	public Result<Object> setLogged(String sessionUuid, String traceId) {
		LOGGER.info("setLogged sessionUuid is {}, traceId is {}", sessionUuid, traceId);
		boolean result = inno72AuthInfoService.setLogged(sessionUuid);
		LOGGER.info("setLogged result is {}", result);
		if (result) {
			return Results.success();
		} else {
			return Results.failure("登录失败");
		}
	}

	/**
	 * 登录前的操作（目前聚石塔回调）
	 */
	@ResponseBody
	@RequestMapping(value = "/processBeforeLogged", method = {RequestMethod.POST})
	public Result<Object> processBeforeLogged(String sessionUuid, String authInfo, String traceId) {

		Result<Object> result = null;
		try {
			result = inno72AuthInfoService.processBeforeLogged(sessionUuid, authInfo, traceId);
		}catch (Exception e){
			LOGGER.error("processBeforeLogged error ",e);
			result = Results.failure(e.getMessage());
		}
		return result;
	}

    /**
     * 登录跳转
     */
    @ResponseBody
    @RequestMapping(value = "/loginRedirect", method = {RequestMethod.GET})
    public Result loginRedirect(HttpServletRequest request,HttpServletResponse response, String sessionUuid, String env,Integer channelType) {
        LOGGER.info("loginRedirect sessionUuid is {}, env is {}", sessionUuid, env);
        Result result = null;
        try {
            synchronized (this) {
                String redirectUrl = "";
                // 判断是否已经有人扫过了，如果扫过 直接跳转
                UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);

                if (sessionVo != null) {
                    LOGGER.info("loginRedirect isScanned is {}", sessionVo.getIsScanned());

                    // 判断二维码是否已经超时, 恢复isScanned 状态 为false，允许二维码继续被扫
                    boolean qrCode = gameSessionRedisUtil.exists(sessionUuid + "qrCode");
                    LOGGER.info("loginRedirect qrCode is {}", qrCode);
                    if (!qrCode) {
                        sessionVo.setIsScanned(false);
                    }
					result = Results.success(!sessionVo.getIsScanned());
                    if (sessionVo.getIsScanned()) {
                        LOGGER.info("loginRedirect 二维码已经被扫描");
                        redirectUrl = String.format(topH5ErrUrl, env) + "/?status="+ TopH5ErrorTypeEnum.IS_SCANNED.getValue();
                    } else {
                        sessionVo.setIsScanned(true);
                        //发送推送消息
						sendMsg(sessionUuid,Inno72MachineConnectionMsg.TYPE_ENUM.SCANQRCODE.getKey());
                        String traceId = UuidUtil.getUUID32();
                        sessionVo.setTraceId(traceId);
                        sessionVo.setScanUrl(redirectUrl);
                        // 设置15秒内二维码不能被扫
                        gameSessionRedisUtil.setSessionEx(sessionUuid + "qrCode", sessionUuid, 15);

                        pointService.innerPoint(JSON.toJSONString(sessionVo), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.SCAN_LOGIN);
						if(channelType!=null && channelType == StandardLoginTypeEnum.INNO72.getValue()){
							String tmalFlag = request.getParameter("tmalFlag");
							boolean wrapFlag = false;
							if(!StringUtils.isEmpty(tmalFlag)&&"1".equals(tmalFlag)){
								redirectUrl = String.format(inno72GameServiceProperties.get("phoneLoginUrlTmal"),sessionVo.getPlanCode(),sessionUuid);
							}else{
								redirectUrl = String.format(inno72GameServiceProperties.get("phoneLoginUrl"),sessionVo.getPlanCode(),sessionUuid);
								if(env.equals("prod")) wrapFlag = true;
							}
							String PU = request.getParameter("PU");
							if(!StringUtils.isEmpty(PU)){
								redirectUrl+="&PU="+PU;
							}
							redirectUrl+="&activityId="+sessionVo.getActivityId();
							LOGGER.info("loginRedirect loginUrl={}",redirectUrl);
							if(wrapFlag){
								redirectUrl = wrapWechatUrl(redirectUrl);
							}
						}else{
							redirectUrl = String.format("%s%s/%s/%s", inno72GameServiceProperties.get("tmallUrl"), sessionUuid, env, traceId);
						}
                    }
                }
                LOGGER.info("loginRedirect redirectUrl is {} ", redirectUrl);
                if(channelType == null || channelType == StandardLoginTypeEnum.INNO72.getValue()){
                    response.sendRedirect(redirectUrl);
                    return null;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

	private void sendMsg(String sessionUuid,Integer type) {
    	LOGGER.info("sendMsg sessionUuid={},type={}",sessionUuid,type);
		UserSessionVo sessionVo = new UserSessionVo(sessionUuid);
		Long version = System.currentTimeMillis();
		String machineCode = sessionVo.getMachineCode();
		String activityId = sessionVo.getActivityId();
    	switch(type){
			case 1:
				Inno72ConnectionScanVo inno72ConnectionScanVo = new Inno72ConnectionScanVo();
				inno72ConnectionScanVo.setActivityId(activityId);
				inno72ConnectionScanVo.setMachineCode(machineCode);
				inno72ConnectionScanVo.setVersion(version);
				inno72ConnectionScanVo.setType(type);
				inno72ConnectionService.send(machineCode,activityId,version,type,JsonUtil.toJson(inno72ConnectionScanVo));
				break;
			case 2:
				Inno72ConnectionLoginVo data = new Inno72ConnectionLoginVo();
				data.setActivityId(activityId);
				data.setMachineCode(machineCode);
				data.setVersion(version);
				data.setCanOrder(sessionVo.getCanOrder());
				data.setCountGoods(sessionVo.getCountGoods());
				data.setUserNick(sessionVo.getUserNick());
				data.setType(type);
				inno72ConnectionService.send(machineCode,activityId,version,type,JsonUtil.toJson(data));
				break;
			case 3:
				Inno72ConnectionFollowVo inno72ConnectionFollowVo = new Inno72ConnectionFollowVo();
				inno72ConnectionFollowVo.setActivityId(activityId);
				inno72ConnectionFollowVo.setMachineCode(machineCode);
				inno72ConnectionFollowVo.setVersion(version);
				inno72ConnectionFollowVo.setType(type);
				inno72ConnectionService.send(machineCode,activityId,version,type,JsonUtil.toJson(inno72ConnectionFollowVo));
				break;

		}
	}

	private String wrapWechatUrl(String redirectUrl) throws UnsupportedEncodingException {
		String url = URLEncoder.encode(redirectUrl,"UTF-8");
		String retUrl = "https://open.weixin.qq.com/connect/oauth2/authorize?appid=wxd2d020e170a05549&redirect_uri="+url+"&response_type=code&scope=snsapi_userinfo&state=STATE#wechat_redirect";
		LOGGER.info("wrapWechatUrl ={}",retUrl);
		return retUrl;
	}

	/**
	 * 支付跳转
	 */
	@ResponseBody
	@RequestMapping(value = "/payRedirect", method = {RequestMethod.GET})
	public void payRedirect(HttpServletResponse response, String sessionUuid, String env) {
		LOGGER.info("payRedirect sessionUuid is {}, env is {}", sessionUuid, env);

		// 判断支付二维码是否已经过期
		UserSessionVo sessionVo = gameSessionRedisUtil.getSessionKey(sessionUuid);
 		if (sessionVo != null) {
			String redirectUrl = "";
			boolean payCode = gameSessionRedisUtil.exists(sessionUuid + "payCode");
			if (payCode) {
				// 存在的情况 直接跳转天猫支付
				redirectUrl = sessionVo.getScanPayUrl();
			} else {
				// 不存在 说明已经过期 todo gxg point 需要跳转到对应提示页面
				redirectUrl = String.format(topH5ErrUrl, env) + "/?status="+ TopH5ErrorTypeEnum.IS_SCANNED.getValue();
			}
			try {
				LOGGER.info("payRedirect redirectUrl is {}", redirectUrl);
				response.sendRedirect(redirectUrl);
			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 入会操作（目前聚石塔回调）
	 */
	@ResponseBody
	@RequestMapping(value = "/newRetailmemberJoin", method = {RequestMethod.POST})
	public Result<Object> newRetailmemberJoin(String sessionUuid,String sellSessionKey,String taobaoUserId,String meberJoinCallBackUrl) {
		return inno72GameApiService.newRetailmemberJoin(sessionUuid,sellSessionKey,taobaoUserId,meberJoinCallBackUrl);
	}
    @RequestMapping(value = "/concern_callback", method = {RequestMethod.GET, RequestMethod.POST})
    public Result<String> concernCallback(HttpServletResponse response, HttpServletRequest request,
                                          String sessionUuid, String tbResult, String redirectUrl)  {
        LOGGER.info("关注店铺回调参数 {}", JSON.toJSONString(request.getParameterMap()));
        try {
            if (StringUtils.isNotEmpty(tbResult) && tbResult.equals("1")){
                UserSessionVo sessionKey = gameSessionRedisUtil.getSessionKey(sessionUuid);
                if (sessionKey == null){
                    return Results.failure("session 过期！");
                }
                String msg = "用户["+sessionKey.getUserNick()+"]关注店铺成功.";

				// 调用关注接口
				String sellerId = sessionKey.getSellerId();
				LOGGER.info("concernCallback sellerId is {}", sellerId);
				if (!StringUtil.isEmpty(sellerId)) {
					pointService.innerPoint(JSON.toJSONString(sessionKey), Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE.CONCERN);
					inno72TopService.fllowshopLog(sessionUuid, sellerId);
				}

                CommonBean.logger(
                        CommonBean.POINT_TYPE_CONCERN,
                        sessionKey.getMachineCode(),
                        msg,
                        sessionKey.getActivityId()
                );
            }

			inno72AuthInfoService.setFollowed(sessionUuid);
            sendMsg(sessionUuid,Inno72MachineConnectionMsg.TYPE_ENUM.FOLLOW.getKey());

            response.sendRedirect(URLDecoder.decode(redirectUrl, java.nio.charset.StandardCharsets.UTF_8.toString()));
        } catch (IOException e) {
            LOGGER.error("关注店铺回调异常 {}, {}",e.getMessage(), e);
        }
        return Results.success();
    }

}
