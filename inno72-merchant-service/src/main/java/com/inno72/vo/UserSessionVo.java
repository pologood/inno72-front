package com.inno72.vo;

import com.inno72.model.Inno72MerchantUser;

import lombok.Data;

@Data
public class UserSessionVo extends Inno72MerchantUser {
	private String token;
}
