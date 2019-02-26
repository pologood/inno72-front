package com.inno72.vo;

import java.util.ArrayList;
import java.util.List;

import javax.validation.constraints.NotNull;

import com.alibaba.fastjson.JSON;

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

	public static void main(String[] args) {
		Inno72StoreVo vo = new Inno72StoreVo();
		List<Inno72StoreOrderVo> order = new ArrayList<>();
		order.add(new Inno72StoreOrderVo());
		order.add(new Inno72StoreOrderVo());
		vo.setOrder(order);
		System.out.println(JSON.toJSONString(vo));
	}

	public List<Inno72StoreOrderVo> getOrder() {
		if (this.order == null) {
			return new ArrayList<>();
		}
		return order;
	}
}
