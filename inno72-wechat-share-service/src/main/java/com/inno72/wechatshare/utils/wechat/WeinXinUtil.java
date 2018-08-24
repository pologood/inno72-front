package com.inno72.wechatshare.utils.wechat;

import net.sf.json.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

@Component
public class WeinXinUtil {
    private static final Logger LOGGER = LoggerFactory.getLogger(WeinXinUtil.class);

    private static String APPID = "wx112e5d49fcce8a44";// 第三方用户唯一凭证
    private static String SECRET = "243c1b08ac9b415aecdec76a8963703b";// 第三方用户唯一凭证密钥，即appsecret
    private static String ACCESS_TOKEN_URL="https://api.weixin.qq.com/cgi-bin/token?grant_type=client_credential&appid="+APPID+"&secret="+SECRET; // 获取access_token的接口地址（GET） 限2000（次/天）
    private static String TICKET_URL = "https://api.weixin.qq.com/cgi-bin/ticket/getticket?access_token=";


    private static String TOKEN_KEY = "wechat-share:token";
    private static String TICKET_KEY = "wechat-share:ticket";

    Object tokenLock = new Object();

    Object ticketLock = new Object();

    @Autowired
    private StringRedisTemplate template;

    public WinXinEntity getWinXinEntity(String url) {
        WinXinEntity wx = new WinXinEntity();
        String access_token = getAccessToken();
        String ticket = getTicket(access_token);
        //TODO 缓存ticket
        Map<String, String> ret = Sign.sign(ticket, url);
        wx.setTicket(ret.get("jsapi_ticket"));
        wx.setSignature(ret.get("signature"));
        wx.setNoncestr(ret.get("nonceStr"));
        wx.setTimestamp(ret.get("timestamp"));
        wx.setAppid(APPID);
        return wx;
    }
    private String getAccessToken() {
        String accessToken = template.opsForValue().get(TOKEN_KEY);
        if(StringUtils.isEmpty(accessToken)){
            synchronized (tokenLock){
                accessToken = template.opsForValue().get(TOKEN_KEY);
                if(StringUtils.isEmpty(accessToken)){
                    String retValue = getAccessTokenFromWx();
                    JSONObject demoJson = JSONObject.fromObject(retValue);
                    accessToken = demoJson.getString("access_token");
                    Long expires = demoJson.getLong("expires_in");
                    template.opsForValue().set(TOKEN_KEY,accessToken,expires,TimeUnit.SECONDS);
                }
            }
        }
        return accessToken;
    }
    // 获取token
    private String getAccessTokenFromWx() {
        String message = null;
        InputStream is = null;
        try {
            URL urlGet = new URL(ACCESS_TOKEN_URL);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            /*
             * System.setProperty("sun.net.client.defaultConnectTimeout",
             * "30000");// 连接超时30秒
             * System.setProperty("sun.net.client.defaultReadTimeout", "30000");
             * // 读取超时30秒
             */
            http.connect();
            is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            message = new String(jsonBytes, "UTF-8");
            LOGGER.info("getAccessToken result ="+message);
            is.close();
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("getAccessToken",e);
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return message;
    }
    private String getTicket(String access_token) {
        String ticket = template.opsForValue().get(TICKET_KEY);
        if(StringUtils.isEmpty(ticket)){
            synchronized (ticketLock){
                ticket = template.opsForValue().get(TICKET_KEY);
                if(StringUtils.isEmpty(ticket)){
                    String retValue = getTicketFromWx(access_token);
                    JSONObject demoJson = JSONObject.fromObject(retValue);
                    ticket = demoJson.getString("ticket");
                    Long expires = demoJson.getLong("expires_in");
                    template.opsForValue().set(TICKET_KEY,ticket,expires,TimeUnit.SECONDS);
                }
            }
        }
        return ticket;
    }
    // 获取ticket
    private String getTicketFromWx(String access_token) {
        String message = null;
        String url = TICKET_URL + access_token + "&type=jsapi";// 这个url链接和参数不能变
        InputStream is = null;
        try {
            URL urlGet = new URL(url);
            HttpURLConnection http = (HttpURLConnection) urlGet.openConnection();
            http.setRequestMethod("GET"); // 必须是get方式请求
            http.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            http.setDoOutput(true);
            http.setDoInput(true);
            System.setProperty("sun.net.client.defaultConnectTimeout", "10000");// 连接超时10秒
            System.setProperty("sun.net.client.defaultReadTimeout", "10000"); // 读取超时10秒
            http.connect();
            is = http.getInputStream();
            int size = is.available();
            byte[] jsonBytes = new byte[size];
            is.read(jsonBytes);
            message = new String(jsonBytes, "UTF-8");
            LOGGER.info("getTicket result ="+message);
        } catch (Exception e) {
            e.printStackTrace();
            LOGGER.error("getTicket",e);
        }finally{
            if(is!=null){
                try {
                    is.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return message;
    }
}