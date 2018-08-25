package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Coupon;

public interface Inno72CouponMapper extends Mapper<Inno72Coupon> {
	Inno72Coupon selectCouponCodeByParam(Map<String,String> selectCouponParam);
}