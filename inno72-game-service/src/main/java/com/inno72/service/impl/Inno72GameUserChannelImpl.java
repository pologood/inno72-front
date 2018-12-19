package com.inno72.service.impl;

import com.alibaba.fastjson.JSON;
import com.inno72.common.StandardLoginTypeEnum;
import com.inno72.common.json.JsonUtil;
import com.inno72.mapper.Inno72ChannelMapper;
import com.inno72.mapper.Inno72GameUserChannelMapper;
import com.inno72.mapper.Inno72GameUserMapper;
import com.inno72.model.Inno72Channel;
import com.inno72.model.Inno72GameUser;
import com.inno72.model.Inno72GameUserChannel;
import com.inno72.service.Inno72GameUserChannelService;
import com.inno72.vo.WxMpUser;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class Inno72GameUserChannelImpl implements Inno72GameUserChannelService {

    @Autowired
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
    @Autowired
    private Inno72GameUserMapper inno72GameUserMapper;
    @Autowired
    private Inno72ChannelMapper inno72ChannelMapper;
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
}
