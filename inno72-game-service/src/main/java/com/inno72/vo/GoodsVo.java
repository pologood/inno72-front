package com.inno72.vo;

import java.util.ArrayList;
import java.util.List;

public class GoodsVo {

	private String goodsId;
	private int goodsNum;
	private String goodsName;
	private List<String> channelId;


	public GoodsVo(String goodsId, int goodsNum, String goodsName) {
		super();
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.goodsName = goodsName;
		this.channelId = new ArrayList<>();
	}

	public GoodsVo() {
		super();
	}

	public String getGoodsId() {
		return goodsId;
	}

	public void setGoodsId(String goodsId) {
		this.goodsId = goodsId;
	}

	public int getGoodsNum() {
		return goodsNum;
	}

	public void setGoodsNum(int goodsNum) {
		this.goodsNum = goodsNum;
	}

	public String getGoodsName() {
		return goodsName;
	}

	public void setGoodsName(String goodsName) {
		this.goodsName = goodsName;
	}

	public List<String> getChannelId() {
		return channelId;
	}

	public void setChannelId(List<String> channelId) {
		this.channelId = channelId;
	}
}
