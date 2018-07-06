package com.inno72.controller;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.taobao.api.DefaultTaobaoClient;
import com.taobao.api.TaobaoClient;
import com.taobao.api.request.*;
import com.taobao.api.response.*;
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

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inno72.util.JsonUtils;
import com.inno72.vo.UserInfo;
import com.taobao.api.ApiException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class TopController {

    Logger logger = LoggerFactory.getLogger(TopController.class);

//    private static String url;
//
//    private static String appkey;
//
//    private static String secret;

    public static final String URL = "https://eco.taobao.com/router/rest";
    public static final String APPKEY = "24791535";
    public static final String SECRET = "c0799e02efbb606288c51f02a987ba43";


    @Value("${game_server_url}")
    private String gameServerUrl;

    @Value("${h5_mobile_url}")
    private String h5MobileUrl;

    public static final String APP_NAME = "tivm";

    private TaobaoClient client = new DefaultTaobaoClient(URL, APPKEY, SECRET);

    /**
     * 登录回调接口
     */
    @RequestMapping("/api/top/{mid}/{sessionUuid}")
    public void home(HttpServletRequest request, HttpServletResponse response, @PathVariable("mid") String mid, @PathVariable("sessionUuid") String sessionUuid, String code) {
        logger.info("mid is {}, code is {}, sessionUuid is {}", new String[]{mid, code, sessionUuid});

        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

            String authInfo = getAuthInfo(code);
            logger.info("authInfo is {}", authInfo);

            String tokenResult = JsonUtils.getString(authInfo, "token_result");
            logger.info("tokenResult is {}", tokenResult);

            String taobaoUserId = JsonUtils.getString(tokenResult, "taobao_user_nick");
            logger.info("taobaoUserId is {}", taobaoUserId);

            UserInfo userInfo = new UserInfo();
            userInfo.setMid(mid);
            userInfo.setSessionUuid(sessionUuid);
            userInfo.setCode(code);
            userInfo.setUserId(taobaoUserId);

            userInfo.setToken(tokenResult);
            // 设置用户信息
            setUserInfo(userInfo);
        }
        try {
            // 跳转到游戏页面 手机端redirect
            response.sendRedirect(h5MobileUrl);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取脱敏用户名称
     * http://open.taobao.com/doc2/apiDetail.htm?apiId=35602
     */
    @RequestMapping(value="/api/top/getMaskUserNick",method=RequestMethod.POST)
    private String getMaskUserNick(String accessToken, String mid, Long sellerId) {
        logger.info("getMaskUserNick accessToken is {}, mid is {}, sellerId is {}", new Object[]{accessToken, mid, sellerId});
        TmallFansAutomachineGetmaskusernickRequest req = new TmallFansAutomachineGetmaskusernickRequest();
        req.setSellerId(sellerId);
        req.setMachineId(mid);
        req.setAppName(APP_NAME);
        TmallFansAutomachineGetmaskusernickResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        String result = rsp.getBody();
        logger.info("getMaskUserNick result is {}", result);
        return result;
    }

    /**
     * 调用游戏服务器接口设置关联 sessionUuid authInfo信息
     */
    private void setUserInfo(UserInfo userInfo) {
        RestTemplate client = new RestTemplate();
        MultiValueMap<String, Object> postParameters = new LinkedMultiValueMap<String, Object>();
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
            result = client.postForObject(gameServerUrl, requestEntity, String.class);
            logger.info("setUserInfo result is {} ", result);
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }
    }

    /**
     * 上报日志接口
     * http://open.taobao.com/doc2/apiDetail.htm?apiId=36653
     */
    @RequestMapping(value="/api/top/addLog",method=RequestMethod.POST)
    public String addLog(String accessToken) {
        logger.info("addLog accessToken is {}", accessToken);
        TmallFansAutomachineOrderAddlogRequest req = new TmallFansAutomachineOrderAddlogRequest();
        TmallFansAutomachineOrderAddlogRequest.LogReqrest obj1 = new TmallFansAutomachineOrderAddlogRequest.LogReqrest();
        obj1.setValue1("233232");
        obj1.setValue2("code");
        obj1.setValue3(21L);
        obj1.setValue4(1L);
        obj1.setItemId(39489384L);
        obj1.setSellerId(3834893L);
        obj1.setUserId(2323232L);
        obj1.setType("play_time");
        obj1.setBizCode("automachine");
        req.setLogRequest(obj1);
        TmallFansAutomachineOrderAddlogResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            String result = rsp.getBody();
            logger.info("addLog result is {}", result);
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    @RequestMapping(value="/api/top/order",method=RequestMethod.POST)
    public String order(String accessToken, String mid, String activityId, Long goodsId) {
        logger.info("order accessToken is {}, mid is {}, activityId is {}, goodsId is {}", new Object[]{accessToken, mid, activityId, goodsId});
        TmallFansAutomachineOrderCreateorderbyitemidRequest req = new TmallFansAutomachineOrderCreateorderbyitemidRequest();
        req.setActivityId(activityId);
        req.setUseDiscount(true);
        req.setSkuId(0L);
        req.setItemId(goodsId);
        req.setMachineId(mid);
        try {
            TmallFansAutomachineOrderCreateorderbyitemidResponse rsp = client.execute(req, accessToken);
            String result = rsp.getBody();
            logger.info("order result is {}", result);
            return rsp.getBody();
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 互动吧自动售卖机订单付款状态查询接口
     * http://open.taobao.com/api.htm?docId=34774&docType=2
     */
    @RequestMapping(value="/api/top/order-polling",method=RequestMethod.POST)
    public String orderPolling(String accessToken, String orderId) {
        TmallFansAutomachineOrderCheckpaystatusRequest req = new TmallFansAutomachineOrderCheckpaystatusRequest();
        req.setOrderId(Long.valueOf(orderId));
        TmallFansAutomachineOrderCheckpaystatusResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            String result = rsp.getBody();
            logger.info("orderPolling result is {}", result);
            return rsp.getBody();
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * isv抽奖接口
     * http://open.taobao.com/docs/api.htm?spm=a1z6v.8204065.c3.125.bznYh6&apiId=27640
     */
    @RequestMapping(value="/api/top/lottory",method=RequestMethod.POST)
    public String lottory(String sessionKey, String interactId, Long shopId, String ua, String umid) {
        TmallInteractIsvlotteryDrawRequest req = new TmallInteractIsvlotteryDrawRequest();
        req.setAsac("1A18228U6DKEXP78A4PAT4"); // todo gxg 更换 appkey 后需确认
        req.setUa(ua);
        req.setUmid(umid);
        req.setInteractId(interactId);
        req.setShopId(shopId);
        TmallInteractIsvlotteryDrawResponse rsp = null;
        try {
            rsp = client.execute(req, sessionKey);
            String result = rsp.getBody();
            logger.info("lottory result is {}", result);
            return result;
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 获取Access Token
     * http://open.taobao.com/api.htm?spm=a219a.7386797.0.0.ZOlSkP&source=search&docId=25388&docType=2
     */
    public String getAuthInfo(String code) {
        logger.info("getAuthInfo code is {}", code);
        TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
        req.setCode(code);
        TopAuthTokenCreateResponse rsp;
        try {
            rsp = client.execute(req);
            String result = rsp.getBody();
            logger.info("getAuthInfo result is {}", result);
            return result;
        } catch (ApiException e) {
            logger.error(e.getMessage(), e);
        }
        return "";
    }

    /**
     * 自动售卖机出货记录保存
     * http://open.taobao.com/doc2/apiDetail.htm?apiId=35763
     */
    @RequestMapping(value="/api/top/deliveryRecord",method=RequestMethod.POST)
    public String deliveryRecord(String accessToken, String mid, String orderId, String channelId) {
        logger.info("deliveryRecord accessToken is {}, mid is {}, orderId is {}, channelId is {}", new String[]{accessToken, mid, orderId, channelId});
        TmallFansAutomachineDeliveryrecordRequest req = new TmallFansAutomachineDeliveryrecordRequest();
        req.setOrderId(Long.valueOf(orderId));
        req.setMachineId(mid);
        req.setChannelId(channelId);
        TmallFansAutomachineDeliveryrecordResponse rsp = null;
        try {
            rsp = client.execute(req, accessToken);
            String result = rsp.getBody();
            logger.info("deliveryRecord result is", result);
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/api/top/index")
    public String index(HttpServletRequest request) throws JsonProcessingException {
        logger.info("index");
        return "index";
    }

//    @Value("${url}")
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    @Value("${appkey}")
//    public void setAppkey(String appkey) {
//        this.appkey = appkey;
//    }
//
//    @Value("${secret}")
//    public void setSecret(String secret) {
//        this.secret = secret;
//    }
}
