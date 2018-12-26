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

    /**
     * 关联微信用户和手机号用户
     * @param openId
     * @param phone
     * @return
     */
    String joinUser(String openId, String phone);

    /**
     * 更新用户信息
     * @param user
     */
    void updateWechatUser(WxMpUser user,String userChannelId);

    Inno72GameUserChannel buildWechatUser(WxMpUser user);

    void save(Inno72GameUserChannel gameUserChannel);

    void update(Inno72GameUserChannel userChannel);
}
