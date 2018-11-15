package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.service.Inno72MerchantUserService;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class Inno72MerchantUserController {

	@Resource
	private Inno72MerchantUserService inno72MerchantUserService;

	@RequestMapping(value = "/inno72/merchant/login")
	public Result login(String userName, String password) {
		return inno72MerchantUserService.login(userName, password);
	}

	/**
	 * 重置密码
	 * @param password 新密码
	 * @param phone 发送信息的手机号
	 * @return 结果
	 */
	@RequestMapping(value = "/inno72/merchant/resetPwd")
	public Result resetPwd(String password, String userName, String phone, String token) {
		return inno72MerchantUserService.resetPwd(password, userName, phone, token);
	}

	@RequestMapping(value = "/inno72/merchant/alterPwd")
	public Result alterPwd(String id, String password, String oPassword) {
		return inno72MerchantUserService.alterPwd(id, password, oPassword);
	}

	@RequestMapping(value = "/inno72/merchant/resetPhone")
	public Result resetPhone(String id, String newMobile, String token) {
		return inno72MerchantUserService.resetPhone(id, newMobile, token);
	}

	@RequestMapping(value = "/inno72/merchant/user/detail")
	public Result detail(@RequestParam Integer id) {
		Inno72MerchantUser inno72MerchantUser = inno72MerchantUserService.findById(id);
		return ResultGenerator.genSuccessResult(inno72MerchantUser);
	}

	@RequestMapping(value = "/inno72/merchant/checkCode")
	public Result checkCode(String phone, String code){

		return inno72MerchantUserService.checkPhone(phone, code);
	}
	@RequestMapping(value = "/inno72/merchant/checkMerchant")
	public Result checkMerchant(String phone, String userName){
		return inno72MerchantUserService.checkMerchant(phone, userName);
	}
}
