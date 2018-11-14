package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantUser;

public interface Inno72MerchantUserMapper extends Mapper<Inno72MerchantUser> {
	Inno72MerchantUser selectByNameAndPwd(Map<String, String> param);

	List<Inno72MerchantUser> selectByMobile(String phone);

	int updatePwdByPhone(Inno72MerchantUser newUser);

	Inno72MerchantUser selectByLoginNameAndPhone(@Param("phone") String phone, @Param("loginName") String loginName);
}