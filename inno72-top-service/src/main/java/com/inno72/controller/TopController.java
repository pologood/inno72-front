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
import com.taobao.api.request.TmallFansAutomachineDeliveryrecordRequest;
import com.taobao.api.request.TmallFansAutomachineGetmaskusernickRequest;
import com.taobao.api.request.TmallFansAutomachineOrderAddlogRequest;
import com.taobao.api.request.TmallFansAutomachineOrderCheckpaystatusRequest;
import com.taobao.api.request.TmallFansAutomachineOrderCreateorderbyitemidRequest;
import com.taobao.api.request.TmallInteractIsvlotteryDrawRequest;
import com.taobao.api.request.TmallMarketingFaceSkindetectRequest;
import com.taobao.api.request.TopAuthTokenCreateRequest;
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
	private TaobaoClient client;

	@PostConstruct
	public void initClient(){
		client = new DefaultTaobaoClient(propertiesBean.getUrl(), propertiesBean.getAppkey(), propertiesBean.getSecret());
	}

	/**
	 * 登录回调接口
	 */
	@RequestMapping("/api/top/{mid}/{sessionUuid}/{env}")
	public void home(HttpServletResponse response, @PathVariable("mid") String mid,
			@PathVariable("sessionUuid") String sessionUuid, String code, @PathVariable("env") String env) throws Exception {
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
			data = setUserInfo(userInfo, env);
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
//			String h5Url = this.getHostGameH5Url(env);
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
	 * 获取脱敏用户名称
	 * http://open.taobao.com/doc2/apiDetail.htm?apiId=35602
	 */
	@RequestMapping(value = "/api/top/getMaskUserNick", method = RequestMethod.POST)
	private String getMaskUserNick(String accessToken, String mid, Long sellerId) throws ApiException {
		// @formatter:off
		LOGGER.info("getMaskUserNick accessToken is {}, mid is {}, sellerId is {}", accessToken, mid, sellerId);
		// @formatter:on
		Validators.checkParamNotNull(accessToken, mid, sellerId);

		TmallFansAutomachineGetmaskusernickRequest req = new TmallFansAutomachineGetmaskusernickRequest();
		req.setSellerId(sellerId);
		req.setMachineId(mid);
		req.setAppName(APP_NAME);
		TmallFansAutomachineGetmaskusernickResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("getMaskUserNick result is {}", result);
		return result;
	}

	/**
	 * 调用游戏服务器接口设置关联 sessionUuid authInfo信息
	 */
	private String setUserInfo(UserInfo userInfo, String env) {
		LOGGER.info("gameServerUrl is " + gameServerUrl);
		RestTemplate client = new RestTemplate();
		MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<>();
		postParameters.add("sessionUuid", userInfo.getSessionUuid());
		postParameters.add("mid", userInfo.getMid());
		postParameters.add("code", userInfo.getCode());
		postParameters.add("token", userInfo.getToken());
		postParameters.add("userId", userInfo.getUserId());
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
		LOGGER.info("addLog accessToken is {}", accessToken);
		Validators.checkParamNotNull(accessToken, logReqrest);

		TmallFansAutomachineOrderAddlogRequest req = new TmallFansAutomachineOrderAddlogRequest();
		req.setLogRequest(logReqrest);
		TmallFansAutomachineOrderAddlogResponse rsp = client.execute(req, accessToken);
		String result = rsp.getBody();
		LOGGER.info("addLog result is {}", result);
		return result;
	}

	@RequestMapping(value = "/api/top/order", method = RequestMethod.POST)
	public String order(String accessToken, String mid, String activityId, Long goodsId) throws ApiException {
		LOGGER.info("order accessToken is {}, mid is {}, activityId is {}, goodsId is {}", accessToken, mid, activityId,
				goodsId);
		Validators.checkParamNotNull(accessToken, mid, activityId, goodsId);

		TmallFansAutomachineOrderCreateorderbyitemidRequest req = new TmallFansAutomachineOrderCreateorderbyitemidRequest();
		req.setActivityId(activityId);
		req.setUseDiscount(true);
		req.setSkuId(0L);
		req.setItemId(goodsId);
		req.setMachineId(mid);
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
		req.setAsac("1A18228U6DKEXP78A4PAT4"); // todo gxg 更换 appkey 后需确认
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
		return "test";
//		return JSON.toJSONString(client);
	}

	/**
	 *
	 * @param image 图片的base64（必须以base64,开头）	base64,xxx
	 * @param mixnick 用户mixnick t01A+8NLodWrUz+M3lESGlKf6Fzup0APmo56QGE4282SaY=
	 * @param source 	isv标识 	isv_001
	 * @param front_camera 1	前置摄像头1，后置摄像头0
	 * @param sessionKey sessionKey
	 * @return body
	 */
	@RequestMapping("/api/top/{sessionUuid}")
	public String skindetect(String image, String mixnick, String source, String front_camera, @PathVariable("sessionUuid") String sessionKey){
		LOGGER.info("肌肤检测接口参数 image = {}; mixnich = {}; source = {}; front_camera = {}; sessionUuid = {}"
		,image, mixnick, source, front_camera, sessionKey);
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
		}catch (ApiException e){
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

//	private String getHostGameH5Url(String startWith) {
//		String format = MessageFormat.format(h5MobileUrl, propertiesBean.getValue(startWith + "HostMobile"));
//		LOGGER.info("获取环境 变量组装的game Url {} , env {}", format, startWith);
//		return format;
//
//	}
}
