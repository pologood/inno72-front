package com.inno72.controller;

import java.io.IOException;
import java.text.MessageFormat;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.inno72.util.Escape;
import com.inno72.util.FastJsonUtils;
import com.inno72.validator.Validators;
import com.inno72.vo.FansActVo;
import com.inno72.vo.MachineVo;
import com.inno72.vo.PropertiesBean;
import com.inno72.vo.UserInfo;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CrmMemberIdentityGetRequest;
import com.taobao.api.request.CrmMemberJoinurlGetRequest;
import com.taobao.api.request.TmallFansAutomachineDeliveryrecordRequest;
import com.taobao.api.request.TmallFansAutomachineGetmaskusernickRequest;
import com.taobao.api.request.TmallFansAutomachineOrderAddlogRequest;
import com.taobao.api.request.TmallFansAutomachineOrderCheckpaystatusRequest;
import com.taobao.api.request.TmallFansAutomachineOrderCreateorderbyitemidRequest;
import com.taobao.api.request.TmallInteractIsvlotteryDrawRequest;
import com.taobao.api.request.TmallMarketingFaceSkindetectRequest;
import com.taobao.api.request.TopAuthTokenCreateRequest;
import com.taobao.api.response.CrmMemberIdentityGetResponse;
import com.taobao.api.response.CrmMemberJoinurlGetResponse;
import com.taobao.api.response.TmallFansAutomachineDeliveryrecordResponse;
import com.taobao.api.response.TmallFansAutomachineGetmaskusernickResponse;
import com.taobao.api.response.TmallFansAutomachineOrderAddlogResponse;
import com.taobao.api.response.TmallFansAutomachineOrderCheckpaystatusResponse;
import com.taobao.api.response.TmallFansAutomachineOrderCreateorderbyitemidResponse;
import com.taobao.api.response.TmallInteractIsvlotteryDrawResponse;
import com.taobao.api.response.TmallMarketingFaceSkindetectResponse;
import com.taobao.api.response.TopAuthTokenCreateResponse;

@RestController
public class TopController {

	private static final Logger LOGGER = LoggerFactory.getLogger(TopController.class);

	@Resource
	private PropertiesBean propertiesBean;

	private static final String APP_NAME = "点72互动";
	@Value("${game_server_url}")
	private String gameServerUrl;
	@Value("${h5_mobile_url}")
	private String h5MobileUrl;
	@Value("${jst_url}")
	private String jstUrl;
	private TaobaoClient client;
	private TaobaoClient samplinghClient;

	@PostConstruct
	public void initClient() {
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(),
				propertiesBean.getSecret());
		samplinghClient = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getSampLingAppkey(),
				propertiesBean.getSecret());
	}


	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/top/{sessionUuid}/{env}")
	public void topIndex(HttpServletResponse response,
			@PathVariable("sessionUuid") String sessionUuid, String code, @PathVariable("env") String env)
			throws Exception {
		LOGGER.info("code is {}, sessionUuid is {}, env is {}", code, sessionUuid, env);
		String playCode = "";
		String data;
		String qrStatus = "";
		String sellerId = "";
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = getAuthInfo(code);
			LOGGER.debug("authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("tokenResult is {}", tokenResult);

			String taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("taobaoUserId is {}", taobaoUserId);

			UserInfo userInfo = new UserInfo();
			userInfo.setSessionUuid(sessionUuid);
			userInfo.setAuthInfo(tokenResult);

			data = this.processBeforeLogged(userInfo, env);
			LOGGER.info("processBeforeLogged result is {}", data);

			String machineCode = "";
			String goodsCode = "";
			String isVip = "";
			String sessionKey = "";

			if (!StringUtils.isEmpty(data)) {
				playCode = FastJsonUtils.getString(data, "playCode");
				qrStatus = FastJsonUtils.getString(data, "qrStatus");
				String sId = FastJsonUtils.getString(data, "sellerId");
				machineCode = FastJsonUtils.getString(data, "machineCode");
				goodsCode = FastJsonUtils.getString(data, "goodsCode");
				isVip = FastJsonUtils.getString(data, "isVip");
				sessionKey = FastJsonUtils.getString(data, "sessionKey");

				if (!StringUtils.isEmpty(sId)) {
					sellerId = sId.trim();
				}
			}

			// todo gxg 判断是否入会
			if (!StringUtils.isEmpty(isVip) && Integer.valueOf(isVip) == 1) {
				LOGGER.info("派样活动逻辑");
				// 派样活动逻辑
				String identityResBody = this.memberIdentity(machineCode, goodsCode, taobaoUserId, sessionKey);

				LOGGER.info("identityResBody is {}", identityResBody);
				String grade_name = FastJsonUtils.getString(identityResBody, "grade_name");
				LOGGER.info("grade_name is {}", grade_name);

				String formatUrl = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId=" + sellerId;

				LOGGER.info("formatUrl is {}", formatUrl);
				if (grade_name == null || "".equals(grade_name)) {

					String meberJoinCallBackUrl = jstUrl + "/api/meberJoinCallBack/" + sessionUuid + "/" + env + "/" + playCode + "/"
									+ qrStatus + "/" + sellerId;
					LOGGER.info("meberJoinCallBackUrl is {}", meberJoinCallBackUrl);

//					// 如果不是会员做入会操作
//					String memberJoinResBody = memberJoin(mid, code, sessionUuid, env, itemId, isVip, sessionKey,
//							meberJoinCallBackUrl);
//					LOGGER.info("memberJoinResBody is {}", memberJoinResBody);
//					String resultUrl = FastJsonUtils.getString(memberJoinResBody, "result");
//					LOGGER.info("resultUrl is {}", resultUrl);
//					response.sendRedirect("http:" + resultUrl);

				} else {
					// 设置用户已登录
					boolean logged = this.setUserLogged(sessionUuid, env);
					LOGGER.info("logged is {}", logged);
					// 是会员直接跳转h5页面
					response.sendRedirect(formatUrl);
				}

			} else {
				// 正常逻辑
				String logged = this.setLogged(sessionUuid, env);
			}

			LOGGER.info("data is {}", data);
		}
		try {
			// String h5Url = this.getHostGameH5Url(env);
			// 跳转到游戏页面 手机端redirect
			LOGGER.info("h5MobileUrl is {} , playCode is {}, env is {}", h5MobileUrl, playCode, env);
			String formatUrl = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId="
					+ sellerId;
			LOGGER.info("formatUrl is {}", formatUrl);
			response.sendRedirect(formatUrl);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}


	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/top/{mid}/{sessionUuid}/{env}")
	public void home(HttpServletResponse response, @PathVariable("mid") String mid,
			@PathVariable("sessionUuid") String sessionUuid, String code, @PathVariable("env") String env)
			throws Exception {
		LOGGER.info("mid is {}, code is {}, sessionUuid is {}, env is {}", mid, code, sessionUuid, env);
		String playCode = "";
		String data;
		String qrStatus = "";
		String sellerId = "";
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = getAuthInfo(code);
			LOGGER.debug("authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("tokenResult is {}", tokenResult);

			String taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("taobaoUserId is {}", taobaoUserId);

			UserInfo userInfo = new UserInfo();
			userInfo.setMid(mid);
			userInfo.setSessionUuid(sessionUuid);
			userInfo.setCode(code);
			userInfo.setUserId(taobaoUserId);

			userInfo.setToken(tokenResult);

			// 设置用户信息
			data = setUserInfo(userInfo, env, null);
			LOGGER.info("data is {}", data);

			if (!StringUtils.isEmpty(data)) {
				playCode = FastJsonUtils.getString(data, "playCode");
				qrStatus = FastJsonUtils.getString(data, "qrStatus");
				String sId = FastJsonUtils.getString(data, "sellerId");
				if (!StringUtils.isEmpty(sId)) {
					sellerId = sId.trim();
				}
			}
		}
		try {
			// String h5Url = this.getHostGameH5Url(env);
			// 跳转到游戏页面 手机端redirect
			LOGGER.info("h5MobileUrl is {} , playCode is {}, env is {}", h5MobileUrl, playCode, env);
			String formatUrl = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId=" + sellerId;
			LOGGER.info("formatUrl is {}", formatUrl);
			response.sendRedirect(formatUrl);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 登录前处理
	 * processBeforeLogged
	 */
	private String processBeforeLogged(UserInfo userInfo, String env) {
		LOGGER.info("gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", userInfo.getSessionUuid());
		postParameters.add("authInfo", userInfo.getAuthInfo());
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			String gameserverUrl = propertiesBean.getValue(env + "HostGame");
			LOGGER.info("gameserverUrl is {}", gameserverUrl);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(gameserverUrl + "/api/standard/processBeforeLogged/", requestEntity, String.class);
			LOGGER.info("setUserInfo result is {} ", result);

			String code = FastJsonUtils.getString(result, "code");
			String data = FastJsonUtils.getString(result, "data");
			LOGGER.info("code is {}, data is {}", code, data);
			if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(data) && code.equals("0")) {
				LOGGER.info("setUserInfo gameId is {} ", data);
				return data;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 登录前处理
	 * processBeforeLogged
	 */
	private String setLogged(String sessionUuid, String env) {
		LOGGER.info("setLogged sessionUuid is " + sessionUuid);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid",sessionUuid);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {

			String gameserverUrl = propertiesBean.getValue(env + "HostGame");
			LOGGER.info("gameserverUrl ", gameserverUrl);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(gameserverUrl + "/api/standard/setLogged/", requestEntity, String.class);
			LOGGER.info("setLogged result is {} ", result);

		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 获取脱敏用户名称
	 * http://open.taobao.com/doc2/apiDetail.htm?apiId=35602
	 */
	@RequestMapping(value = "/api/top/getMaskUserNick", method = RequestMethod.POST)
	private String getMaskUserNick(String accessToken, String mid, Long sellerId, String mixNick) throws ApiException {
		// @formatter:off
		LOGGER.info("getMaskUserNick accessToken is {}, mid is {}, sellerId is {}, mixNick is {}", accessToken, mid, sellerId, mixNick);
		// @formatter:on
		Validators.checkParamNotNull(accessToken, mid, sellerId);

		TmallFansAutomachineGetmaskusernickRequest req = new TmallFansAutomachineGetmaskusernickRequest();
		req.setSellerId(sellerId);
		req.setMixNick(mixNick);
		req.setMachineId(mid);
		// req.setAppName(APP_NAME);
		TmallFansAutomachineGetmaskusernickResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("getMaskUserNick result is {}", result);
		return result;
	}

	/**
	 * 调用游戏服务器接口设置关联 sessionUuid authInfo信息
	 */
	private String setUserInfo(UserInfo userInfo, String env, String itemId) {
		LOGGER.info("gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", userInfo.getSessionUuid());
		postParameters.add("mid", userInfo.getMid());
		postParameters.add("code", userInfo.getCode());
		postParameters.add("token", userInfo.getToken());
		postParameters.add("userId", userInfo.getUserId());
		postParameters.add("itemId", itemId);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(this.getHostGameUrl(env), requestEntity, String.class);
			LOGGER.info("setUserInfo result is {} ", result);

			String code = FastJsonUtils.getString(result, "code");
			String data = FastJsonUtils.getString(result, "data");
			LOGGER.info("code is {}, data is {}", code, data);
			if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(data) && code.equals("0")) {
				LOGGER.info("setUserInfo gameId is {} ", data);
				return data;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 上报日志接口
	 * http://open.taobao.com/doc2/apiDetail.htm?apiId=36653
	 */
	@RequestMapping(value = "/api/top/addLog", method = RequestMethod.POST)
	public String addLog(String accessToken, TmallFansAutomachineOrderAddlogRequest.LogReqrest logReqrest)
			throws ApiException {
		LOGGER.info("addLog accessToken is {}, logReqrest is {} ", accessToken, FastJsonUtils.toJson(logReqrest));
		Validators.checkParamNotNull(accessToken, logReqrest);

		TmallFansAutomachineOrderAddlogRequest req = new TmallFansAutomachineOrderAddlogRequest();
		logReqrest.setBizCode("automachine");
		req.setLogRequest(logReqrest);
		TmallFansAutomachineOrderAddlogResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("addLog result is {}", result);
		return result;
	}

	@RequestMapping(value = "/api/top/order", method = RequestMethod.POST)
	public String order(String accessToken, String mid, String activityId, Long goodsId, String mixNick)
			throws ApiException {
		LOGGER.info("order accessToken is {}, mid is {}, activityId is {}, goodsId is {}, mixNick is {} ", accessToken,
				mid, activityId, goodsId, mixNick);
		Validators.checkParamNotNull(accessToken, mid, activityId, goodsId);

		TmallFansAutomachineOrderCreateorderbyitemidRequest req = new TmallFansAutomachineOrderCreateorderbyitemidRequest();
		req.setActivityId(activityId);
		req.setUseDiscount(true);
		req.setSkuId(0L);
		req.setItemId(goodsId);
		req.setMachineId(mid);
		req.setMixnick(mixNick);
		TmallFansAutomachineOrderCreateorderbyitemidResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("order result is {}", result);
		return result;
	}

	/**
	 * 互动吧自动售卖机订单付款状态查询接口
	 * http://open.taobao.com/api.htm?docId=34774&docType=2
	 */
	@RequestMapping(value = "/api/top/order-polling", method = RequestMethod.POST)
	public String orderPolling(String accessToken, String orderId) throws ApiException {
		LOGGER.info("orderPolling accessToken is {}, orderId is {}", accessToken, orderId);
		Validators.checkParamNotNull(accessToken, orderId);

		TmallFansAutomachineOrderCheckpaystatusRequest req = new TmallFansAutomachineOrderCheckpaystatusRequest();
		req.setOrderId(Long.valueOf(orderId));
		TmallFansAutomachineOrderCheckpaystatusResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("orderPolling result is {}", result);
		return result;
	}

	/**
	 * isv抽奖接口
	 * http://open.taobao.com/docs/api.htm?spm=a1z6v.8204065.c3.125.bznYh6&apiId=27640
	 */
	@RequestMapping(value = "/api/top/lottory", method = RequestMethod.POST)
	public String lottory(String accessToken, String interactId, Long shopId, String ua, String umid)
			throws ApiException {
		LOGGER.info("lottory accessToken is {}, interactId is {}, shopId is {}, ua is {}, umid is {}", accessToken,
				interactId, shopId, ua, umid);
		Validators.checkParamNotNull(accessToken, interactId, shopId, ua, umid);

		TmallInteractIsvlotteryDrawRequest req = new TmallInteractIsvlotteryDrawRequest();
		req.setAsac("1A18228U6DKEXP78A4PAT4");
		req.setUa(ua);
		req.setUmid(umid);
		req.setInteractId(interactId);
		req.setShopId(shopId);
		TmallInteractIsvlotteryDrawResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("lottory result is {}", result);
		return result;
	}

	/**
	 * 获取Access Token
	 * http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.ZOlSkP&source=search&docId=25388&docType=2
	 */
	private String getAuthInfo(String code) throws ApiException {
		LOGGER.info("getAuthInfo code is {}", code);
		Validators.checkParamNotNull(code);

		TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
		req.setCode(code);
		TopAuthTokenCreateResponse rsp;
		rsp = client.execute(req);
		String result = rsp.getBody();
		LOGGER.info("getAuthInfo result is {}", result);
		return result;
	}

	/**
	 * 自动售卖机出货记录保存
	 * http://open.taobao.com/doc2/apiDetail.htm?apiId=35763
	 */
	@RequestMapping(value = "/api/top/deliveryRecord", method = RequestMethod.POST)
	public String deliveryRecord(String accessToken, String mid, String orderId, String channelId) throws ApiException {
		LOGGER.info("deliveryRecord accessToken is {}, mid is {}, orderId is {}, channelId is {}", accessToken, mid,
				orderId, channelId);
		Validators.checkParamNotNull(accessToken, mid, orderId, channelId);

		TmallFansAutomachineDeliveryrecordRequest req = new TmallFansAutomachineDeliveryrecordRequest();
		req.setOrderId(Long.valueOf(orderId));
		req.setMachineId(mid);
		req.setChannelId(channelId);
		TmallFansAutomachineDeliveryrecordResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("deliveryRecord result is", result);
		return result;
	}

	@RequestMapping("/api/top/index")
	public String index(String test) {
		Validators.checkParamNotNull(test);
		LOGGER.info("index");
		return "index";
	}

	@RequestMapping("/test")
	public String test() {
		LOGGER.info("test -----");
		return "hahatest";
		// return JSON.toJSONString(client);
	}

	/**
	 *
	 * @param image 图片的base64（必须以base64,开头）	base64,xxx
	 * @param mixnick 用户mixnick t01A+8NLodWrUz+M3lESGlKf6Fzup0APmo56QGE4282SaY=
	 * @param source    isv标识 	isv_001
	 * @param front_camera 1	前置摄像头1，后置摄像头0
	 * @param sessionKey sessionKey
	 * @return body
	 */
	@RequestMapping("/api/top/{sessionUuid}")
	public String skindetect(String image, String mixnick, String source, String front_camera,
			@PathVariable("sessionUuid") String sessionKey) {
		LOGGER.info("肌肤检测接口参数 image = {}; mixnich = {}; source = {}; front_camera = {}; sessionUuid = {}", image,
				mixnick, source, front_camera, sessionKey);
		TmallMarketingFaceSkindetectRequest req = new TmallMarketingFaceSkindetectRequest();
		req.setImage(image);
		req.setMixnick(mixnick);
		req.setSource(source);
		req.setFrontCamera(front_camera);

		String body = "";
		try {
			TmallMarketingFaceSkindetectResponse rsp = client.execute(req, sessionKey);
			LOGGER.info("调用肌肤检测完整结果 =================》 {}", JSON.toJSONString(rsp));
			body = rsp.getBody();
		} catch (ApiException e) {
			LOGGER.error("调用肌肤检测接口失败 ===> {} {}", e.getMessage(), e);
		}
		LOGGER.info("图片解析结果 ===> {}", body);
		return body;
	}

	private String getHostGameUrl(String startWith) {
		String format = MessageFormat.format(gameServerUrl, propertiesBean.getValue(startWith + "HostGame"));
		LOGGER.info("获取环境 变量组装的game Url {} , env {}", format, startWith);
		return format;
	}

	/**
	 * API名称:tmall.fans.automachine.saveact( 保存更新活动信息至天猫 ) 前台类目:互动吧API
	 * API用户授权类型:不需要 API安全等级:W1
	 * API标签:
	 * 收费策略: 简要描述:保存更新活动信息至天猫
	 *
	 * API 应用级输入参数
	 * owner_id Long 必填 供应商为一ID，淘宝ID 23434234
	 * machine_v_o MachineVo 必填 设备信息
	 *
	 * @return  {
	 * 			  "tmall_fans_automachine_saveact_response":{
	 *  			"model":true,
	 *  			"msg_info":"参数错误",
	 *				"msg_code":"SUCCESS"
	 *			  }
	 *			}
	 *
	 *	model    Boolean 否 付款成功状态，true成功，false为失败 true
	 *  msg_info String  否 请求失败时的错误信息 参数错误
	 *  msg_code String  否 SUCCESS为请求成功，其他为请求失败 SUCCESS
	 *
	 */
	@RequestMapping("/tmall/fans/automachine/saveact")
	private Object saveact(@RequestBody FansActVo request) {


		return JSON.toJSONString(request);
	}

	/**
	 * API名称:tmall.fans.automachine.savemachine( 注册、更新供应商上的设备信息到天猫互动吧 ) 前台类目:互动吧API
	 * API用户授权类型:需要 API安全等级:W1
	 * API标签:
	 * 收费策略: 简要描述:注册、更新供应商上的设备信息到天猫互动吧
	 *
	 * @return  {
	 * 			  "tmall_fans_automachine_savemachine_response":{
	 *  			"model":true,
	 *  			"msg_info":"参数错误",
	 *				"msg_code":"SUCCESS"
	 *			  }
	 *			}
	 */
	@RequestMapping("/tmall/fans/automachine/savemachine")
	private Object savemachine(@RequestBody MachineVo request) {

		return JSON.toJSONString(request);
	}

	/**
	 * 派样活动登录回调接口
	 */
	@RequestMapping("/api/samplingTop/{mid}/{sessionUuid}/{env}/{itemId}/{isVip}/{sessionKey}")
	public void samplingHome(HttpServletResponse response, @PathVariable("mid") String mid,
			@PathVariable("sessionUuid") String sessionUuid, String code, @PathVariable("env") String env,
			@PathVariable("itemId") String itemId, @PathVariable("isVip") String isVip,
			@PathVariable("sessionKey") String sessionKey) throws Exception {
		LOGGER.info(
				"samplingHome mid is {}, code is {}, sessionUuid is {}, env is {}, ItemId is {}, isVip is {}, sessionKey is {}",
				mid, code, sessionUuid, env, itemId, isVip, sessionKey);
		String playCode = "";
		String data;
		String qrStatus = "";
		String sellerId = "";
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = getAuthInfo(code);
			LOGGER.debug("authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("tokenResult is {}", tokenResult);

			String taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("taobaoUserId is {}", taobaoUserId);

			UserInfo userInfo = new UserInfo();
			userInfo.setMid(mid);
			userInfo.setSessionUuid(sessionUuid);
			userInfo.setCode(code);
			userInfo.setUserId(taobaoUserId);

			userInfo.setToken(tokenResult);


			try {
				// 判断当前店铺是否需要入会
				if ("1".equals(isVip)) {

					// 设置用户信息
					data = setUserInfo(userInfo, env, itemId);
					LOGGER.info("data is {}", data);

					if (!StringUtils.isEmpty(data)) {
						playCode = FastJsonUtils.getString(data, "playCode");
						qrStatus = FastJsonUtils.getString(data, "qrStatus");
						String sId = FastJsonUtils.getString(data, "sellerId");
						if (!StringUtils.isEmpty(sId)) {
							sellerId = sId.trim();
						}
					}

					// String h5Url = this.getHostGameH5Url(env);
					// 跳转到游戏页面 手机端redirect
					LOGGER.info("h5MobileUrl is {} , playCode is {}, env is {}", h5MobileUrl, playCode, env);
					String formatUrl =
							String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId="
									+ sellerId;
					LOGGER.info("formatUrl is {}", formatUrl);

					String meberJoinCallBackUrl =
							jstUrl + "/api/meberJoinCallBack/" + sessionUuid + "/" + env + "/" + playCode + "/"
									+ qrStatus + "/" + sellerId;
					LOGGER.info("meberJoinCallBackUrl is {}", meberJoinCallBackUrl);

					// 判断当前用户是否为会员
					String identityResBody = memberIdentity(mid, itemId, taobaoUserId, sessionKey);
					LOGGER.info("identityResBody is {}", identityResBody);
					String grade_name = FastJsonUtils.getString(identityResBody, "grade_name");
					LOGGER.info("grade_name is {}", grade_name);
					if (grade_name == null || "".equals(grade_name)) {
						// 如果不是会员做入会操作
						String memberJoinResBody = memberJoin(mid, code, sessionUuid, env, itemId, isVip, sessionKey,
								meberJoinCallBackUrl);
						LOGGER.info("memberJoinResBody is {}", memberJoinResBody);
						String resultUrl = FastJsonUtils.getString(memberJoinResBody, "result");
						LOGGER.info("resultUrl is {}", resultUrl);
						response.sendRedirect("http:" + resultUrl);

					} else {
						// 设置用户已登录
						boolean logged = this.setUserLogged(sessionUuid, env);
						LOGGER.info("logged is {}", logged);
						// 是会员直接跳转h5页面
						response.sendRedirect(formatUrl);
					}
				}

			} catch (IOException e) {
				LOGGER.error(e.getMessage(), e);
			}
		}
	}

	/**
	 * 入会回调
	 */
	@RequestMapping("/api/meberJoinCallBack/{sessionUuid}/{env}/{playCode}/{qrStatus}/{sellerId}")
	public void meberJoinCallBack(HttpServletResponse response, @PathVariable("sessionUuid") String sessionUuid,
			@PathVariable("env") String env, @PathVariable("playCode") String playCode,
			@PathVariable("qrStatus") String qrStatus, @PathVariable("sellerId") String sellerId) {

		LOGGER.info(
				"meberJoinCallBack params sessionUuid is {}, env is {}, playCode is {}, qrStatus is {}, sellerId is {}",
				sessionUuid, env, playCode, qrStatus, sellerId);

		// 设置用户已登录
		boolean logged = this.setUserLogged(sessionUuid, env);
		LOGGER.info("meberJoinCallBack logged is {} ", logged);

		String h5url = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId=" + sellerId;
		LOGGER.info("meberJoinCallBack h5url is {} ", h5url);
		try {
			// 跳转 手机h5
			response.sendRedirect(h5url);
		} catch (IOException e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 调用游戏服务器接口，设置用户已经登录
	 */
	private boolean setUserLogged(String sessionUuid, String env) {
		LOGGER.info("gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", sessionUuid);
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			String h5Url = propertiesBean.getValue(env + "HostGame") + "/api/setUserLogged";
			LOGGER.info("url is {}", h5Url);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(h5Url, requestEntity, String.class);
			LOGGER.info("setUserLogged result is {} ", result);

			String code = FastJsonUtils.getString(result, "code");
			String data = FastJsonUtils.getString(result, "data");
			LOGGER.info("code is {}, data is {}", code, data);
			if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(data) && code.equals("0")) {
				return true;
			}
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return false;
	}


	/**
	 * 入会
	 */
	private String memberJoin(String mid, String code, String sessionUuid, String env, String itemId, String isVip,
			String sessionKey, String callbackUrl) {

		LOGGER.info(
				"mid is {}, code is {}, sessionUuid is {}, env is {}, ItemId is {}, isVip is {}, sessionKey is {}，callbackUrl is{}",
				mid, code, sessionUuid, env, itemId, isVip, sessionKey, callbackUrl);
		CrmMemberJoinurlGetRequest req = new CrmMemberJoinurlGetRequest();
		// String callbackUrl = jstUrl + mid + "/" + sessionUuid + "/" + env + "/" + itemId + "/" + isVip + "/"
		// + sessionKey + "/1=1?code=" + code;
		String extraInfo = "{\"source\":\"paiyangji\",\"deviceId\":\"" + mid + "\",\"itemId\":" + itemId + "}";
		req.setCallbackUrl(callbackUrl);
		LOGGER.info("callbackUrl is {} ", callbackUrl);
		req.setExtraInfo(extraInfo);
		CrmMemberJoinurlGetResponse rsp = null;
		try {
			rsp = samplinghClient.execute(req, sessionKey);
			LOGGER.info("ruhuirsp is {}", rsp);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return rsp.getBody();
	}

	/**
	 * 是否是会员
	 */
	private String memberIdentity(String machineCode, String itemId, String nickName, String sessionKey) {

		LOGGER.info("machineCode is {}, itemId is {}, nickName is {}, sessionKey is {} ", machineCode, itemId, nickName, sessionKey);
		CrmMemberIdentityGetRequest req = new CrmMemberIdentityGetRequest();
		String extraInfo = "{\"source\":\"paiyangji\",\"deviceId\":\"" + machineCode + "\",\"itemId\":" + itemId + "}";
		req.setExtraInfo(extraInfo);
		req.setMixNick(Escape.unescape(nickName));
		CrmMemberIdentityGetResponse rsp = null;
		try {
			rsp = samplinghClient.execute(req, sessionKey);
			LOGGER.info("memberIdentity rsp is {}", rsp);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return rsp.getBody();
	}
}