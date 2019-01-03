package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72Merchant;

public interface Inno72MerchantMapper extends Mapper<Inno72Merchant> {

	List<String> selectMerchantId(String merchantId);
}