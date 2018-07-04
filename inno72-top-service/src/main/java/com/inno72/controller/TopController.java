package com.inno72.controller;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.inno72.util.JsonUtils;
import com.inno72.util.TaobaoClientUtil;
import com.inno72.vo.UserInfo;
import com.taobao.api.ApiException;
import com.taobao.api.request.TmallFansAutomachineOrderCheckpaystatusRequest;
import com.taobao.api.request.TmallFansAutomachineOrderCreateorderbyitemidRequest;
import com.taobao.api.request.TmallInteractIsvlotteryDrawRequest;
import com.taobao.api.request.TopAuthTokenCreateRequest;
import com.taobao.api.response.TmallFansAutomachineOrderCheckpaystatusResponse;
import com.taobao.api.response.TmallFansAutomachineOrderCreateorderbyitemidResponse;
import com.taobao.api.response.TmallInteractIsvlotteryDrawResponse;
import com.taobao.api.response.TopAuthTokenCreateResponse;

@RestController
@RequestMapping(value = "/api")
public class TopController {
	

    @RequestMapping("/api/top/{mid}")
    public void home(HttpServletRequest request, HttpServletResponse response, @PathVariable("mid") String mid) {

        String code = request.getParameter("code");
        System.out.println("code is" + code);

        String sessionUuid = request.getParameter("sessionUuid");
        System.out.println("sessionUuid is " + sessionUuid);

        System.out.println("mid is" + mid);

        if (!StringUtils.isEmpty(code) && !StringUtils.isEmpty(sessionUuid)) {

            String authInfo = getAuthInfo(code);
            System.out.println("authInfo is " + authInfo);

            String tokenResult = JsonUtils.getString(authInfo, "token_result");
            System.out.println("token is " + tokenResult);

            String taobaoUserId = JsonUtils.getString(tokenResult, "taobao_user_id");
            System.out.println("taobaoUserId is " + taobaoUserId);

            UserInfo userInfo = new UserInfo();
            userInfo.setSessionUuid(sessionUuid);
            userInfo.setMid(mid);
            UserInfo.AuthInfo _authInfo  = new UserInfo.AuthInfo();
            _authInfo.setCode(code);


            String _tokenResult = null;

            String encoding = System.getProperty("file.encoding");
            System.out.println("Default System Encoding:" + encoding);

            try {
                _tokenResult = new String(tokenResult.getBytes(encoding), "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            System.out.println("_tokenResult" + _tokenResult);

            // _authInfo.setToken("test");

            _authInfo.setToken(_tokenResult);
            _authInfo.setUserId(taobaoUserId);
            userInfo.setAuthInfo(_authInfo);
            // 调用游戏服务器接口
            System.out.println("flag1");
            setUserInfo(userInfo);
            System.out.println("flag2");
        }

        // 跳转到游戏页面 手机端redirect
        try {
            response.sendRedirect("http://47.95.217.215:9999");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    


    private void setUserInfo(UserInfo userInfo) {
        System.out.println("setUserInfo----");
        String value = "";
        try {
            value = JsonUtils.toJson(userInfo);
            System.out.printf("setUserInfo is " + value);
        } catch (Exception e) {
            e.printStackTrace();
        }

        String url = "http://47.95.217.215:30880/api/sessionRedirect";
        RestTemplate client = new RestTemplate();
        //  一定要设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.postForEntity(url, requestEntity , String.class );
        System.out.println(response.getBody());
    }
    

    @RequestMapping("/api/top/test1")
    public String index(HttpServletRequest request) throws JsonProcessingException {
        return "index";
    }

    @RequestMapping("/api/top/test")
    public String test(HttpServletRequest request) throws JsonProcessingException {

        String url = "http://47.95.217.215:30880/api/sessionRedirect";
        RestTemplate client = new RestTemplate();
        //  一定要设置header
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        UserInfo userInfo = new UserInfo();
        userInfo.setSessionUuid("0014017ff5614149aa2273f6df7c4f0d");
        userInfo.setMid("456");

        UserInfo.AuthInfo _authInfo  = new UserInfo.AuthInfo();
        _authInfo.setCode("isCWfAx5nC4KYE8hH6TtWqfz7q1089761");
//        _authInfo.setToken("{\"w1_expires_in\":0,\"refresh_token_valid_time\":1530602764000,\"taobao_user_nick\":\"t01sNMnCfkSGKksyq9su9nKdqToIqawc1J4IkhX131bUgk%3D\",\"re_expires_in\":0,\"expire_time\":1531466764000,\"token_type\":\"Bearer\",\"access_token\":\"620082546cdeaf108d7b2934a4d8d0f73ZZb30a9659cd09525671323\",\"taobao_open_uid\":\"AAGxHGf-AF4DUnGwDrNbhtyW\",\"w1_valid\":1530604564234,\"refresh_token\":\"6201025f84bfc8147cb7f92d799883ed5ZZa237d4ad9608525671323\",\"w2_expires_in\":0,\"w2_valid\":1530602764234,\"r1_expires_in\":0,\"r2_expires_in\":0,\"r2_valid\":1530602764234,\"r1_valid\":1530604564234,\"taobao_user_id\":\"-1\",\"expires_in\":848091}");
        _authInfo.setToken("test");
        _authInfo.setUserId("-1");
        userInfo.setAuthInfo(_authInfo);

        String value = "";
        try {

            value = JsonUtils.toJson(userInfo);
        } catch (Exception e) {
            e.printStackTrace();
        }


        HttpEntity<String> requestEntity = new HttpEntity<String>(value, headers);
        //  执行HTTP请求
        ResponseEntity<String> response = client.postForEntity(url, requestEntity , String.class );
        System.out.println(response.getBody());

        System.out.println("test --- ");
        return "test ok";
    }

    @RequestMapping("/api/top/order")
    public String order(String sessionKey) {
        System.out.println("order begin sessionKey is" + sessionKey);
        TmallFansAutomachineOrderCreateorderbyitemidRequest req = new TmallFansAutomachineOrderCreateorderbyitemidRequest();
        req.setActivityId("mengniu");
        req.setUseDiscount(true);
        req.setSkuId(0L);
        req.setItemId(571723166234L);
        req.setMachineId("1804040054");
        try {
            TmallFansAutomachineOrderCreateorderbyitemidResponse rsp = TaobaoClientUtil.client.execute(req, sessionKey);
            System.out.println(rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/api/top/order-polling")
    public String orderPolling(String orderId, String sessionKey) {
        TmallFansAutomachineOrderCheckpaystatusRequest req = new TmallFansAutomachineOrderCheckpaystatusRequest();
        req.setOrderId(Long.valueOf(orderId));
        TmallFansAutomachineOrderCheckpaystatusResponse rsp = null;
        try {
            rsp = TaobaoClientUtil.client.execute(req, sessionKey);
            System.out.println(rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "";
    }

    @RequestMapping("/api/top/lottory")
    public String lottory(String sessionKey, String interactId, Long shopId) {
        TmallInteractIsvlotteryDrawRequest req = new TmallInteractIsvlotteryDrawRequest();
        req.setAsac("1A18228U6DKEXP78A4PAT4");

        req.setUa("111aaa");
        req.setUmid("2233bb");

        req.setInteractId(interactId);
        req.setShopId(shopId);
        TmallInteractIsvlotteryDrawResponse rsp = null;
        try {
            rsp = TaobaoClientUtil.client.execute(req, sessionKey);
            System.out.println(rsp.getBody());
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "lottory";
    }

    public String getAuthInfo(String code) {
        TopAuthTokenCreateRequest req = new TopAuthTokenCreateRequest();
        req.setCode(code);
        TopAuthTokenCreateResponse rsp = null;
        try {
            rsp = TaobaoClientUtil.client.execute(req);
            return rsp.getBody();
        } catch (ApiException e) {
            e.printStackTrace();
        }
        return "";
    }


}
