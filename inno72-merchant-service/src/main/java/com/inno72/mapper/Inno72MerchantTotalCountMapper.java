package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCount;

public interface Inno72MerchantTotalCountMapper extends Mapper<Inno72MerchantTotalCount> {

	List<Inno72MerchantTotalCount> selectByMerchantId(String merchantId);
}