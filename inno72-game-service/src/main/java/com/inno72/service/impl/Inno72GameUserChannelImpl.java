package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.google.gson.Gson;
import com.inno72.common.Inno72GameServiceProperties;
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
    private Inno72GameServiceProperties inno72GameServiceProperties;
    @Autowired
    private Inno72GameUserChannelService inno72GameUserChannelService;

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
    public void saveWechatUser(WxMpUser user,String gameUserId) {
        //插入
        if(gameUserId == null){
            Inno72GameUser inno72GameUser = new Inno72GameUser();
            inno72GameUserMapper.insert(inno72GameUser);
            gameUserId = inno72GameUser.getId();
        }
        Inno72Channel channel = inno72ChannelMapper.findByCode(Inno72Channel.CHANNELCODE_WECHAT);
        Inno72GameUserChannel gameUserChannel = new Inno72GameUserChannel();
        gameUserChannel.setChannelId(channel.getId());
        gameUserChannel.setChannelUserKey(user.getUnionId());
        gameUserChannel.setSellerId(user.getAppId());
        gameUserChannel.setChannelName(channel.getChannelName());
        gameUserChannel.setExt(JsonUtil.toJson(user));
        gameUserChannel.setUserNick(user.getNickname());
        gameUserChannel.setLoginType(StandardLoginTypeEnum.WEIXIN.getValue());
        gameUserChannel.setGameUserId(gameUserId);
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
    public void updateWechatUser(WxMpUser user,String gameUserChannelId, String gameUserId) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        if(!StringUtils.isEmpty(gameUserId)){
            param.setGameUserId(gameUserId);
        }
        param.setExt(JsonUtil.toJson(user));
        param.setUserNick(user.getNickname());
        param.setChannelUserKey(user.getUnionId());
        param.setId(gameUserChannelId);
        inno72GameUserChannelMapper.updateByPrimaryKeySelective(param);
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

    @Override
    public WxMpUser getWeChatUserByCode(String code) {
        Map<String, String> requestForm = new HashMap<>();
        requestForm.put("code", code);
        String url = inno72GameServiceProperties.get("wxServiceUrl") + "/getUserByCode";
        LOGGER.info("getWeChatUserByCode url = {},code={}",url,code);
        String result =  HttpClient.form(url, requestForm, null);
        LOGGER.info("getWeChatUserByCode result = {}",result);
        String data = FastJsonUtils.getString(result,"data");
        if(!org.apache.commons.lang3.StringUtils.isEmpty(data)){
            Gson gson = new Gson();
            return gson.fromJson(data,WxMpUser.class);
        }
        return null;
    }

    @Override
    public Inno72GameUserChannel findWeChatUser(String unionId) {
        Inno72GameUserChannel param = new Inno72GameUserChannel();
        param.setChannelUserKey(unionId);
        param.setSellerId(dudujiAppId);
        List<Inno72GameUserChannel> list = inno72GameUserChannelMapper.select(param);
        if(list.size()==0) return null;
        return list.get(0);
    }

    private void savePhoneUser(Inno72Channel inno72Channel, String phone,String gameUserId) {
        String nickName = phone.substring(0, 3) + "****" + phone.substring(7, phone.length());
        Inno72GameUserChannel userChannel = new Inno72GameUserChannel(nickName, phone, inno72Channel.getId(), gameUserId,
                inno72Channel.getChannelName(), phone, null,StandardLoginTypeEnum.INNO72.getValue());
        inno72GameUserChannelMapper.insert(userChannel);
    }
}
