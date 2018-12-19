package com.github.binarywang.demo.wx.mp.service;

import me.chanjar.weixin.mp.bean.result.WxMpUser;

public interface GameServcie {
    /**
     * 转发路由
     * @param user
     * @return
     */
    String redirectAdapter(WxMpUser user);
}
