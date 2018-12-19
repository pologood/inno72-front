package com.inno72.service;

import com.inno72.model.Inno72GameUserChannel;
import com.inno72.vo.WxMpUser;

public interface Inno72GameUserChannelService {
    /**
     * 查找gameuser
     * @param channelId
     * @param channelUserKey
     * @param sellerId
     * @return
     */
    Inno72GameUserChannel findInno72GameUserChannel(String channelId, String channelUserKey, String sellerId);

    /**
     * 保存微信用户
     * @param user
     */
    void saveWechatUser(WxMpUser user);

    Inno72GameUserChannel findByGameUserIdAndChannelId(String gameUserId, String id);
}
