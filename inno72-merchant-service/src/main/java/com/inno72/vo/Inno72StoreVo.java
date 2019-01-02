package com.inno72.vo;

import java.util.List;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Inno72StoreVo {

	@NotNull(message = "活动ID为空!")
	private String activityId;

	@NotNull(message = "商户ID为空!")
	private String merchantId;

	@NotNull(message = "商品ID为空!")
	private String goodsId;

	private List<Inno72StoreOrderVo> order;

}
