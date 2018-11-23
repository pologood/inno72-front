package com.inno72.service;

import com.inno72.model.Inno72GameUserChannel;

public interface Inno72GameUserChannelService {
    /**
     * 查找gameuser
     * @param channelId
     * @param channelUserKey
     * @param sellerId
     * @return
     */
    Inno72GameUserChannel findInno72GameUserChannel(String channelId, String channelUserKey, String sellerId);
}
