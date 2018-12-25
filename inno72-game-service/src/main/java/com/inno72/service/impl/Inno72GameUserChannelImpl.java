package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.inno72.common.Result;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.util.FastJsonUtils;
import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.mapper.Inno72GameUserChannelMapper;
import com.inno72.mapper.Inno72GameUserMapper;
import com.inno72.model.Inno72Channel;
import com.inno72.model.Inno72GameUser;
import com.inno72.model.Inno72GameUserChannel;
import com.inno72.plugin.http.HttpClient;
import com.inno72.service.Inno72GameUserChannelService;
import com.inno72.vo.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@Transactional
public class Inno72GameUserChannelImpl implements Inno72GameUserChannelService {

    private Logger LOGGER = LoggerFactory.getLogger(this.getClass());
    @Autowired
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
    @Autowired
    private Inno72GameUserMapper inno72GameUserMapper;
    @Autowired
    private Inno72ChannelMapper inno72ChannelMapper;
    @Autowired
    private Inno72GameUserChannelService inno72GameUserChannelService;
    @Value("${wechat_server.preurl}")
    private String wechatServerPreurl;

    @Value("${duduji.appid}")
    private String dudujiAppId;
    @Override
    public Inno72GameUserChannel findInno72GameUserChannel(String channelId, String channelUserKey, String sellerId) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        param.setChannelId(channelId);
        param.setChannelUserKey(channelUserKey);
        if(!StringUtils.isEmpty(sellerId)){
            param.setSellerId(sellerId);
        }
        List<Inno72GameUserChannel> list =  inno72GameUserChannelMapper.select(param);
        if(list.size()>0){
            return list.get(0);
        }
        return null;
    }

    @Override
    public void saveWechatUser(WxMpUser user) {
        //插入
        Inno72GameUser inno72GameUser = new Inno72GameUser();
        inno72GameUserMapper.insert(inno72GameUser);
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_WECHAT);
        Inno72GameUserChannel gameUserChannel = new Inno72GameUserChannel();
        gameUserChannel.setChannelId(channel.getId());
        gameUserChannel.setChannelUserKey(user.getOpenId());
        gameUserChannel.setSellerId(user.getAppId());
        gameUserChannel.setChannelName(channel.getChannelName());
        gameUserChannel.setExt(JsonUtil.toJson(user));
        gameUserChannel.setUserNick(user.getNickname());
        gameUserChannel.setLoginType(StandardLoginTypeEnum.WEIXIN.getValue());
        gameUserChannel.setGameUserId(inno72GameUser.getId());
        gameUserChannel.setCreateTime(LocalDateTime.now());
        inno72GameUserChannelMapper.insert(gameUserChannel);
    }

    @Override
    public Inno72GameUserChannel findByGameUserIdAndChannelId(String gameUserId, String channelId) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        param.setChannelId(channelId);
        param.setGameUserId(gameUserId);
        List<Inno72GameUserChannel> list = inno72GameUserChannelMapper.select(param);
        if(list.size() == 0) return null;
        return list.get(0);
    }

    @Override
    public String joinUser(String openId, String phone) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        param.setChannelUserKey(openId);
        param.setSellerId(dudujiAppId);
        List<Inno72GameUserChannel> list = inno72GameUserChannelMapper.select(param);
        Inno72GameUserChannel weixinUser = list.get(0);

        //查找手机号用户
        //查找手机号渠道
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_INNO72);
        param = new Inno72GameUserChannel();
        param.setChannelId(channel.getId());
        param.setChannelUserKey(phone);
        list = inno72GameUserChannelMapper.select(param);
        if(list.size()== 0){
            //保存手机用户
            savePhoneUser(channel,phone,weixinUser.getGameUserId());
        }else{
            Inno72GameUserChannel phoneUser = list.get(0);
            param = new Inno72GameUserChannel();
            param.setId(phoneUser.getId());
            param.setGameUserId(weixinUser.getGameUserId());
            inno72GameUserChannelMapper.updateByPrimaryKeySelective(param);
        }
        return weixinUser.getGameUserId();
    }

    @Override
    public void updateWechatUser(WxMpUser user,String gameUserChannelId) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        param.setExt(JsonUtil.toJson(user));
        param.setUserNick(user.getNickname());
        param.setId(gameUserChannelId);
        inno72GameUserChannelMapper.updateByPrimaryKeySelective(param);
    }

    @Override
    public void bindWeChatAndPhoneUser(String openId,String gameUserId) {
        //获取微信用户
        WxMpUser user = getWeChatUserByOpenId(openId);
        if(user == null) {
            LOGGER.error("无法查找到微信用户openId= {}",openId);
            user = new WxMpUser();
            user.setOpenId(openId);
        }
        user.setAppId(dudujiAppId);
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_WECHAT);
        Inno72GameUserChannel userChannel = inno72GameUserChannelService.findInno72GameUserChannel(channel.getId(),user.getOpenId(),dudujiAppId);
        if(userChannel == null) {
            //插入微信用户
            Inno72GameUserChannel gameUserChannel = buildWechatUser(user);
            gameUserChannel.setGameUserId(gameUserId);
            save(gameUserChannel);
        }else{
            if(!gameUserId.equals(userChannel.getGameUserId())){
                userChannel.setGameUserId(gameUserId);
                update(userChannel);
            }
        }
    }

    private WxMpUser getWeChatUserByOpenId(String openId) {
        Map<String, String> requestForm = new HashMap<>(1);
        requestForm.put("openId",openId);
        String result = HttpClient.form(wechatServerPreurl+"/getUserByOpenId",requestForm,null);
        String code = FastJsonUtils.getString(result,"code");
        if(!StringUtils.isEmpty(code)&&Integer.parseInt(code) == Result.SUCCESS){
            String data = FastJsonUtils.getString(result,"data");
            Gson gson = new Gson();
            return gson.fromJson(data,WxMpUser.class);
        }else{
            LOGGER.error("getWeChatUserByOpenId error result = {}",result);
        }
        return null;
    }

    @Override
    public Inno72GameUserChannel buildWechatUser(WxMpUser user) {
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_WECHAT);
        Inno72GameUserChannel gameUserChannel = new Inno72GameUserChannel();
        gameUserChannel.setChannelId(channel.getId());
        gameUserChannel.setChannelUserKey(user.getOpenId());
        gameUserChannel.setSellerId(user.getAppId());
        gameUserChannel.setChannelName(channel.getChannelName());
        gameUserChannel.setExt(JsonUtil.toJson(user));
        gameUserChannel.setUserNick(user.getNickname());
        gameUserChannel.setLoginType(StandardLoginTypeEnum.WEIXIN.getValue());
        gameUserChannel.setCreateTime(LocalDateTime.now());
        return gameUserChannel;
    }

    @Override
    public void save(Inno72GameUserChannel gameUserChannel) {
        inno72GameUserChannelMapper.insert(gameUserChannel);
    }

    @Override
    public void update(Inno72GameUserChannel userChannel) {
        inno72GameUserChannelMapper.updateByPrimaryKeySelective(userChannel);
    }

    private void savePhoneUser(Inno72Channel inno72Channel, String phone,String gameUserId) {
        String nickName = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        Inno72GameUserChannel userChannel = new Inno72GameUserChannel(nickName, phone, inno72Channel.getId(), gameUserId,
                inno72Channel.getChannelName(), phone, null,StandardLoginTypeEnum.INNO72.getValue());
        inno72GameUserChannelMapper.insert(userChannel);
    }
}
