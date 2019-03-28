package com.inno72.vo;

import java.io.Serializable;

public class Inno72ConnectionPayVo  extends Inno72ConnectionBaseResultVo implements Serializable {
    private static final long serialVersionUID = 152471359234355923L;
    private Integer flag = 1;
    private String goodsCode;
    private String channelCode;
    private String msg;

	public Integer getFlag() {
		return flag;
	}

	public void setFlag(Integer flag) {
		this.flag = flag;
	}

	public String getGoodsCode() {
		return goodsCode;
	}

	public void setGoodsCode(String goodsCode) {
		this.goodsCode = goodsCode;
	}

	public String getChannelCode() {
		return channelCode;
	}

	public void setChannelCode(String channelCode) {
		this.channelCode = channelCode;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}
}