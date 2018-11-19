package com.inno72.service.impl;

import com.inno72.mapper.Inno72GameUserChannelMapper;
import com.inno72.model.Inno72GameUserChannel;
import com.inno72.service.Inno72GameUserChannelService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Inno72GameUserChannelImpl implements Inno72GameUserChannelService {

    @Autowired
    private Inno72GameUserChannelMapper inno72GameUserChannelMapper;
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
}
