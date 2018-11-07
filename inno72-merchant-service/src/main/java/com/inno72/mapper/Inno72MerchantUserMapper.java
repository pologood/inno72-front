package com.inno72.mapper;

import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantUser;

public interface Inno72MerchantUserMapper extends Mapper<Inno72MerchantUser> {
	Inno72MerchantUser selectByNameAndPwd(Map<String, String> param);
}