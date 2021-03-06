package com.inno72.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;

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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.alibaba.fastjson.JSON;
import com.inno72.util.FastJsonUtils;
import com.inno72.validator.Validators;
import com.inno72.vo.PropertiesBean;
import com.inno72.vo.UserInfo;
import com.taobao.api.ApiException;
import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.CrmMemberIdentityGetRequest;
import com.taobao.api.request.CrmMemberJoinurlGetRequest;
import com.taobao.api.request.StoreFollowurlGetRequest;
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
import com.taobao.api.response.StoreFollowurlGetResponse;
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

	@Value("${h5_mobile_err_url}")
	private String h5MobileErrUrl;

	@Value("${jst_url}")
	private String jstUrl;

	@Value("${h5_env_host}")
	private String h5EnvHost;

	private TaobaoClient client;
	private TaobaoClient samplinghClient;

	public static final String RESULT_SUCCESS = "0";
	public static final String RESULT_FAIL = "1";

	public static final Integer IS_VIP = 1;
	public static final Integer IS_NOT_VIP = 0;

	private Map<String, String> envParam;

	@PostConstruct
	public void initClient() {
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(),
				propertiesBean.getSecret());
		samplinghClient = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getSampLingAppkey(),
				propertiesBean.getSecret());
		envParam = new HashMap<>(4);
		envParam.put("dev","dev-game-api");
		envParam.put("test","test-game-api");
		envParam.put("stage","stage-game-api");
		envParam.put("prod","prod-game-api");
	}


	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/top/{sessionUuid}/{env}/{traceId}")
	public void topIndex2(HttpServletResponse response,
			@PathVariable("sessionUuid") String sessionUuid,
			String code,
			@PathVariable("env") String env,
			@PathVariable("traceId") String traceId)
			throws Exception {
		LOGGER.info("topIndex2 code is {}, sessionUuid is {}, env is {}, traceId is {}", code, sessionUuid, env, traceId);
		String playCode = "";
		String result;
		String qrStatus = "";
		String sellerId = "";
		String accessToken = "";
		String followSessionKey = "";
		if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

			String authInfo = getAuthInfo(code);
			LOGGER.debug("topIndex2 authInfo is {}", authInfo);

			String tokenResult = FastJsonUtils.getString(authInfo, "token_result");
			LOGGER.info("topIndex2 tokenResult is {}", tokenResult);

			String taobaoUserId = FastJsonUtils.getString(tokenResult, "taobao_user_nick");
			LOGGER.info("topIndex2 taobaoUserId is {}", taobaoUserId);

			accessToken = FastJsonUtils.getString(tokenResult, "access_token");

			UserInfo userInfo = new UserInfo();
			userInfo.setSessionUuid(sessionUuid);
			userInfo.setAuthInfo(tokenResult);
			userInfo.setTraceId(traceId);

			result = this.processBeforeLogged2(userInfo, env);
			LOGGER.info("topIndex2 processBeforeLogged2 result is {}", result);

			String machineCode = "";
			String goodsCode = "";
			String isVip = "";
			String sessionKey = "";

			if (!StringUtils.isEmpty(result)) {
				String resultCode = FastJsonUtils.getString(result, "code");
				String resultData = FastJsonUtils.getString(result, "data");

				LOGGER.info("resultCode is {}, resultData is {}", resultCode, resultData);

				if (!StringUtils.isEmpty(resultCode) && resultCode.equals(RESULT_SUCCESS)) {
					playCode = FastJsonUtils.getString(result, "playCode");
					qrStatus = FastJsonUtils.getString(result, "qrStatus");
					String sId = FastJsonUtils.getString(result, "sellerId");
					machineCode = FastJsonUtils.getString(result, "machineCode");
					followSessionKey= FastJsonUtils.getString(result, "followSessionKey");
					LOGGER.info("followSessionKey is {}", followSessionKey);

					goodsCode = FastJsonUtils.getString(result, "goodsCode");
					isVip = FastJsonUtils.getString(result, "isVip");
					sessionKey = FastJsonUtils.getString(result, "sessionKey");

					traceId = FastJsonUtils.getString(result, "traceId");
					LOGGER.info("traceId is {}", traceId);

					if (!StringUtils.isEmpty(sId)) {
						sellerId = sId.trim();
					}
				} else if (!StringUtils.isEmpty(resultCode) && resultCode.equals(RESULT_FAIL)) {
					// 跳转到错误页面
					String status = "0";
					String hrErrUrl = this.getH5ErrUrl(env, status);
					LOGGER.info("topIndex2 hrErrUrl is {}" , hrErrUrl);
					response.sendRedirect(hrErrUrl);
				}
			} else {
				String status = "0";
				String hrErrUrl = this.getH5ErrUrl(env, status);
				LOGGER.info("topIndex2 hrErrUrl is {}" , hrErrUrl);
				response.sendRedirect(hrErrUrl);
			}

			// 判断是否入会
			if (!StringUtils.isEmpty(isVip) && Integer.valueOf(isVip) == IS_VIP) {
				LOGGER.info("topIndex2 派样入会逻辑");
				// 派样活动逻辑
				String identityResBody = this.memberIdentity(machineCode, goodsCode, taobaoUserId, sessionKey);

				LOGGER.info("topIndex2 identityResBody is {}", identityResBody);
				String grade_name = FastJsonUtils.getString(identityResBody, "grade_name");
				LOGGER.info("topIndex2 grade_name is {}", grade_name);

				String formatUrl = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId=" + sellerId + "&sessionUuid=" + sessionUuid;

				LOGGER.info("topIndex2 formatUrl is {}", formatUrl);
				if (grade_name == null || "".equals(grade_name)) {

					String meberJoinCallBackUrl = jstUrl + "/api/meberJoinCallBack/" + sessionUuid + "/" + env + "/" + playCode + "/"
							+ qrStatus + "/" + sellerId;
					///api/meberJoinCallBack/{sessionUuid}/{env}/{playCode}/{qrStatus}/{sellerId}/{accessToken}
					LOGGER.info("topIndex2 meberJoinCallBackUrl is {}", meberJoinCallBackUrl);

					// 如果不是会员做入会操作
					String memberJoinResBody = memberJoin(machineCode, code, sessionUuid, env, goodsCode, isVip, sessionKey,
							meberJoinCallBackUrl);
					LOGGER.info("topIndex2 memberJoinResBody is {}", memberJoinResBody);
					String resultUrl = FastJsonUtils.getString(memberJoinResBody, "result");
					LOGGER.info("topIndex2 resultUrl is {}", resultUrl);

					response.sendRedirect("http:" + resultUrl);
					return;
				} else {
					// 设置用户已登录
					boolean logged = this.setUserLogged(sessionUuid, env);
					LOGGER.info("topIndex2 logged is {}", logged);
					// 是会员直接跳转h5页面
				}



			} else {
				// 正常逻辑
				String logged = this.setLogged2(sessionUuid, env, traceId);
				LOGGER.info("topIndex2 logged is {}", logged);
			}

		}
		try {
			// 跳转到游戏页面 手机端redirect
			LOGGER.info("topIndex2 h5MobileUrl is {} , playCode is {}, env is {}", h5MobileUrl, playCode, env);
			String formatUrl = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId="
					+ sellerId + "&method=href&sessionUuid=" + sessionUuid;
			LOGGER.info("topIndex2 formatUrl is {}", formatUrl);

			fllowStoreFlow(response, env, sellerId, sessionUuid, formatUrl);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 关注店铺流程
	 */
	public void fllowStoreFlow(HttpServletResponse response, String env, String sellerId,
			String sessionUuid, String formatUrl) {
		LOGGER.info("env is {}, sellerId is {}, sessionUuid is {}, accessToken is {}, formatUrl is {}",
				env, sellerId, sellerId, formatUrl );
		try {
			String encodeUrl = URLEncoder.encode(formatUrl, java.nio.charset.StandardCharsets.UTF_8.toString());

			StoreFollowurlGetRequest req = new StoreFollowurlGetRequest();
			req.setCallbackUrl(
					h5EnvHost
							+ envParam.get(env)
							+ "/standard/concern_callback?sessionUuid="+sessionUuid
							+ "&redirectUrl="+encodeUrl+"&method=href");
			req.setUserId(Long.parseLong(sellerId));
			LOGGER.info("关注callBackUrl : " + h5EnvHost
					+ envParam.get(env)
					+ "/standard/concern_callback?sessionUuid="+sessionUuid
					+ "&redirectUrl="+encodeUrl+"&method=href");
			StoreFollowurlGetResponse rsp = client.execute(req, "");
			LOGGER.info("fllowStoreFlow  - StoreFollowurlGetResponse - {}", JSON.toJSONString(rsp));
			response.sendRedirect(rsp.getUrl());
		} catch (Exception e) {
			LOGGER.error(e.getMessage() ,e);
		}
	}

	private String getH5ErrUrl(String env, String status) {
		String h5ErrUrl = String.format(h5MobileErrUrl, env, status);
		LOGGER.info("h5ErrUrl is {}", h5ErrUrl);
		return h5ErrUrl;
	}

	/**
	 * 登录前处理 v2
	 * processBeforeLogged
	 */
	private String processBeforeLogged2(UserInfo userInfo, String env) {
		LOGGER.info("processBeforeLogged2 gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", userInfo.getSessionUuid());
		postParameters.add("authInfo", userInfo.getAuthInfo());
		postParameters.add("traceId", userInfo.getTraceId() != null ? userInfo.getTraceId() : "");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			String gameserverUrl = propertiesBean.getValue(env + "HostGame");
			LOGGER.info("processBeforeLogged2 gameserverUrl is {}", gameserverUrl);

			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(gameserverUrl + "/api/standard/processBeforeLogged/", requestEntity, String.class);
			LOGGER.info("processBeforeLogged2 result is {} ", result);
			return result;
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	/**
	 * 登录前处理
	 * processBeforeLogged
	 */
	private String processBeforeLogged(UserInfo userInfo, String env) {
		LOGGER.info("processBeforeLogged gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", userInfo.getSessionUuid());
		postParameters.add("authInfo", userInfo.getAuthInfo());
		postParameters.add("traceId", userInfo.getTraceId() != null ? userInfo.getTraceId() : "");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			String gameserverUrl = propertiesBean.getValue(env + "HostGame");
			LOGGER.info("processBeforeLogged gameserverUrl is {}", gameserverUrl);

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
	 * 登录前处理 v2
	 * processBeforeLogged
	 */
	private String setLogged2(String sessionUuid, String env, String traceId) {
		LOGGER.info("setLogged2 sessionUuid is " + sessionUuid);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", sessionUuid);
		postParameters.add("traceId", traceId);
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
		LOGGER.info("deliveryRecord result is {}", result);
		return result;
	}

	@RequestMapping("/api/top/index")
	public String index(String test) {
		Validators.checkParamNotNull(test);
		LOGGER.info("index");
		return "index";
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
	 * 入会回调
	 * http://inno72top.ews.m.jaeapp.com/api/meberJoinCallBack/18640451/test/20/0/3098056950
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
		// 入会记录日志
		this.log(sessionUuid,env);
		LOGGER.info("meberJoinCallBack logged is {} ", logged);

		String h5url = String.format(h5MobileUrl, env, playCode) + "?qrStatus=" + qrStatus + "&sellerId=" + sellerId + "&sessionUuid=" + sessionUuid;
		LOGGER.info("meberJoinCallBack h5url is {} ", h5url);
		try {
			LOGGER.info("入会回调走关注流程");
			fllowStoreFlow(response, env, sellerId, sessionUuid, h5url);
			// 跳转 手机h5
			// response.sendRedirect(h5url);
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
	}

	/**
	 * 关注
	 * @param accessToken accessToken 淘宝token
	 * @param sessionUuid sessionUuid 机器sessionid
	 *
	 */
	@RequestMapping("/api/top/concern")
	public String concern(String accessToken, String sessionUuid, String env) {

		LOGGER.info( "concern params accessToken is {}, sessionUuid is {}, env is {}", accessToken, sessionUuid, env);
		try {
			StoreFollowurlGetRequest req = new StoreFollowurlGetRequest();
			req.setCallbackUrl(h5EnvHost+envParam.get(env) + "/api/standard/concern_callback?sessionUuid="+sessionUuid);
			StoreFollowurlGetResponse rsp = client.execute(req, accessToken);
			LOGGER.info("活动关注链接 StoreFollowurlGetResponse =》 {}", JSON.toJSONString(rsp));
			return rsp.getBody();
		} catch (Exception e) {
			LOGGER.error(e.getMessage(), e);
		}
		return "";
	}

	private void log(String sessionUuid, String env) {
		LOGGER.info("gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", sessionUuid);
		postParameters.add("type","34");
		HttpHeaders headers = new HttpHeaders();
		headers.add("Content-Type", "application/x-www-form-urlencoded");
		String result;
		try {
			String h5Url = propertiesBean.getValue(env + "HostGame") + "/api/point";
			LOGGER.info("url is {}", h5Url);
			HttpEntity<MultiValueMap<String, Object>> requestEntity = new HttpEntity<>(postParameters, headers);
			result = client.postForObject(h5Url, requestEntity, String.class);
			LOGGER.info("log result = {}",result);
		} catch (Exception e) {
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
		itemId = "10000"; // itemId 暂时写死 ，否则影响 优惠券作为商品时code过长问题
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
		// nickName 需要转义
		req.setMixNick(this.unescape(nickName));
		CrmMemberIdentityGetResponse rsp = null;
		try {
			rsp = samplinghClient.execute(req, sessionKey);
			LOGGER.info("memberIdentity rsp is {}", rsp);
		} catch (ApiException e) {
			e.printStackTrace();
		}
		return rsp.getBody();
	}

	private String unescape(String escapeStr) {
		String decode = "";
		try {
			decode = URLDecoder.decode(escapeStr, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		LOGGER.info("unescape is {}", decode);
		return decode;
	}

}