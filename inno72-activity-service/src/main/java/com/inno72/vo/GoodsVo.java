package com.inno72.vo;

import java.util.ArrayList;
import java.util.List;

public class GoodsVo {

	private String goodsId;
	private int goodsNum;
	private String goodsName;
	private int goodsCount;
	private int goodsRule;
	private List<String> channelIds;


	public GoodsVo(String goodsId, int goodsNum, String goodsName) {
		super();
		this.goodsId = goodsId;
		this.goodsNum = goodsNum;
		this.goodsName = goodsName;
		this.channelIds = new ArrayList<>();
	}

	public GoodsVo() {
		super();
	}

	public int getGoodsCount() {
		return goodsCount;
	}

	public void setGoodsCount(int goodsCount) {
		this.goodsCount = goodsCount;
	}

	public int getGoodsRule() {
		return goodsRule;
	}

	public void setGoodsRule(int goodsRule) {
		this.goodsRule = goodsRule;
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

	public List<String> getChannelIds() {
		return channelIds;
	}

	public void setChannelIds(List<String> channelId) {
		this.channelIds = channelId;
	}
}
