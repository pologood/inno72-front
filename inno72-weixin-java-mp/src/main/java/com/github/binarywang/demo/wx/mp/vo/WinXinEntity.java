package com.github.binarywang.demo.wx.mp.vo;

import me.chanjar.weixin.common.bean.WxJsapiSignature;
import me.chanjar.weixin.mp.bean.result.WxMpUser;

import java.io.Serializable;

public class WinXinEntity implements Serializable {

    private static final long serialVersionUID = 5491240614137503741L;
    private WxJsapiSignature signature;
    private WxMpUser user;

    public WxJsapiSignature getSignature() {
        return signature;
    }

    public void setSignature(WxJsapiSignature signature) {
        this.signature = signature;
    }

    public WxMpUser getUser() {
        return user;
    }

    public void setUser(WxMpUser user) {
        this.user = user;
    }
}
