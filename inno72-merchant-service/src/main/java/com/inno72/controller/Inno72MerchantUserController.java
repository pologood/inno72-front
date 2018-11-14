package com.inno72.controller;

import javax.annotation.Resource;

import org.springframework.web.bind.annotation.CrossOrigin;
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
@CrossOrigin
@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class Inno72MerchantUserController {

	@Resource
	private Inno72MerchantUserService inno72MerchantUserService;

	@RequestMapping(value = "/inno72/merchant/login")
	public Result login(String userName, String password) {
		return inno72MerchantUserService.login(userName, password);
	}

	@RequestMapping(value = "/inno72/merchant/resetPwd")
	public Result resetPwd(String id, String password, String confirm) {
		return inno72MerchantUserService.resetPwd(id, password, confirm);
	}

	@RequestMapping(value = "/inno72/merchant/resetPhone")
	public Result resetPhone(String id, String phone) {
		return inno72MerchantUserService.resetPhone(id, phone);
	}

	@RequestMapping(value = "/inno72/merchant/user/detail")
	public Result detail(@RequestParam Integer id) {
		Inno72MerchantUser inno72MerchantUser = inno72MerchantUserService.findById(id);
		return ResultGenerator.genSuccessResult(inno72MerchantUser);
	}

}
