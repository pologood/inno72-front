package com.inno72.controller;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.service.Inno72MerchantUserService;
import com.inno72.vo.UserSessionVo;

/**
 * Created by CodeGenerator on 2018/11/07.
 */
@RestController
@RequestMapping(method = {RequestMethod.POST, RequestMethod.GET, RequestMethod.OPTIONS})
public class Inno72MerchantUserController {

	@Resource
	private Inno72MerchantUserService inno72MerchantUserService;

	@RequestMapping(value = "/inno72/merchant/login")
	public Result<UserSessionVo> login(String userName, String password) {
		return inno72MerchantUserService.login(userName, password);
	}

	/**
	 * 重置密码
	 * @param password 新密码
	 * @param phone 发送信息的手机号
	 * @return 结果
	 */
	@RequestMapping(value = "/inno72/merchant/resetPwd")
	public Result<String> resetPwd(String password, String userName, String phone, String token) {
		return inno72MerchantUserService.resetPwd(password, userName, phone, token);
	}

	@RequestMapping(value = "/inno72/merchant/alterPwd")
	public Result<String> alterPwd(String id, String password, String oPassword) {
		return inno72MerchantUserService.alterPwd(id, password, oPassword);
	}

	@RequestMapping(value = "/inno72/merchant/resetPhone")
	public Result<String> resetPhone(String id, String newMobile, String token) {
		return inno72MerchantUserService.resetPhone(id, newMobile, token);
	}

	@RequestMapping(value = "/inno72/merchant/user/detail")
	public Result<Inno72MerchantUser> detail(@RequestParam String id) {
		return Results.success(inno72MerchantUserService.findById(id));
	}

	@RequestMapping(value = "/inno72/merchant/checkUser")
	public Result<String> checkUser(String phone, String userName, HttpServletRequest request,
			HttpServletResponse response) {
		Result<String> result = inno72MerchantUserService.checkUser(phone, userName);
		if (result.getCode() == Result.SUCCESS) {
			try {
				request.getRequestDispatcher("/common/code/3?phone=" + phone).forward(request, response);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return result;
	}

	@RequestMapping(value = "/inno72/merchant/checkCode")
	public Result<String> checkCode(String phone, String code) {

		return inno72MerchantUserService.checkPhone(phone, code);
	}

	@RequestMapping(value = "/inno72/merchant/checkMerchant")
	public Result<String> checkMerchant(String phone, String userName) {
		return inno72MerchantUserService.checkMerchant(phone, userName);
	}


	@RequestMapping(value = "/inno72/merchant/selectUser")
	public Result<String> selectUser(String phone, String userName, HttpServletRequest request,
			HttpServletResponse response) {
		Result<String> result = inno72MerchantUserService.selectUser(phone, userName);
		if (result.getCode() == Result.FAILURE) {
			return result;
		}
		try {
			request.getRequestDispatcher("/common/code/3").forward(request, response);
		} catch (Exception e) {
			return Results.failure("请求异常!");
		}
		return result;
	}


}
