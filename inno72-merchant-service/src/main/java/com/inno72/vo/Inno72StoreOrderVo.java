package com.inno72.vo;

import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
public class Inno72StoreOrderVo {

	@NotNull(message = "仓库ID为空!")
	private String storeId;

	@NotNull(message = "仓库名称为空!")
	private String storeName;

	@NotNull(message = "物流名称为空!")
	private String logisticsName;

	@NotNull(message = "物流单号为空!")
	private String logisticsNo;

	@NotNull(message = "商品为空!")
	private Integer goodsNum;

}
