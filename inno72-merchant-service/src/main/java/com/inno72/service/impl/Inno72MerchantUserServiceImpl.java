package com.inno72.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.inno72.common.AbstractService;
import com.inno72.common.CheckParams;
import com.inno72.common.CommonBean;
import com.inno72.common.Result;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.mapper.Inno72MerchantUserMapper;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.redis.IRedisUtil;
import com.inno72.service.Inno72MerchantUserService;
import com.inno72.vo.UserSessionVo;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantUserServiceImpl extends AbstractService<Inno72MerchantUser>
		implements Inno72MerchantUserService {

	private static final Logger LOGGER = LoggerFactory.getLogger(Inno72MerchantUserServiceImpl.class);

	@Resource
	private Inno72MerchantUserMapper inno72MerchantUserMapper;

	@Resource
	private IRedisUtil redisUtil;

	@Override
	public Result<UserSessionVo> login(String userName, String password) {

		if (StringUtil.isEmpty(userName) || StringUtil.isEmpty(password)) {
			return Results.failure("用户名或密码为空!");
		}

		Map<String, String> param = new HashMap<>(2);
		param.put("userName", userName);
		param.put("pwd", CommonBean.pwd(password));

		Inno72MerchantUser user = inno72MerchantUserMapper.selectByNameAndPwd(param);
		if (user == null) {
			return Results.failure("用户名或密码错误!");
		}
		String token = StringUtil.getUUID();

		UserSessionVo vo = new UserSessionVo();
		BeanUtils.copyProperties(user, vo);
		vo.setToken(token);

		redisUtil.setex(CommonBean.REDIS_MERCHANT_LOGIN_SESSION_KEY + token,
				CommonBean.REDIS_MERCHANT_LOGIN_SESSION_KEY_TIMES, JSON.toJSONString(vo));

		return Results.success(vo);
	}

	@Override
	@CheckParams
	public Result<String> resetPwd(String password, String userName, String phone, String code) {
		String s = redisUtil.get(CommonBean.REDIS_MERCHANT_RESET_PWD_KEY + userName);
		if (s == null || !s.equals(code)) {
			return Results.failure("请求异常！");
		}

		List<Inno72MerchantUser> user = inno72MerchantUserMapper.selectByMobile(phone);
		if (user.size() == 0) {
			return Results.failure("用户不存在!");
		}
		LOGGER.info("更新用户密码前 ===> {}", JSON.toJSONString(user));
		Inno72MerchantUser newUser = new Inno72MerchantUser();
		newUser.setPassword(CommonBean.pwd(password));
		newUser.setPhone(phone);
		int i = inno72MerchantUserMapper.updatePwdByPhone(newUser);
		LOGGER.info("更新用户密码完成 ===> {}, {}", JSON.toJSONString(user), i);
		return Results.success();
	}


	@Override
	@CheckParams
	public Result<String> resetPhone(String id, String phone, String token) {
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByPrimaryKey(id);
		if (user == null) {
			return Results.failure("用户不存在!");
		}
		LOGGER.info("更新用户手机号码前 ===> {}", JSON.toJSONString(user));
		user.setPhone(phone);
		int i = inno72MerchantUserMapper.updateByPrimaryKeySelective(user);
		LOGGER.info("更新用户手机号码完成 ===> {}, {}", JSON.toJSONString(user), i);
		return Results.success();
	}

	@Override
	public Result<String> alterPwd(String id, String password, String oPassword) {
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByPrimaryKey(id);
		if (user == null) {
			return Results.failure("用户不存在!");
		}
		if (!user.getPassword().equals(CommonBean.pwd(oPassword))) {
			return Results.failure("原用户密码错误!");
		}
		user.setPassword(CommonBean.pwd(password));
		int i = inno72MerchantUserMapper.updateByPrimaryKeySelective(user);
		LOGGER.info("更新用户密码完成 ===> {}, {}", JSON.toJSONString(user), i);
		return Results.success();
	}

	@Override
	public Result<String> checkMerchant(String phone, String userName) {

		Inno72MerchantUser user = inno72MerchantUserMapper.selectByLoginNameAndPhone(phone, userName);

		if (user == null) {
			return Results.failure("用户不存在!");
		}
		String uuid = StringUtil.uuid();
		redisUtil.setex(CommonBean.REDIS_MERCHANT_RESET_PWD_KEY + userName, 60 * 3, uuid);

		return Results.success(uuid);
	}

	@Override
	public Result<String> checkPhone(String phone, String code) {

		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(code)) {
			return Results.failure("参数为空 !");
		}
		String uuid = StringUtil.uuid();

		redisUtil.setex(CommonBean.REDIS_MERCHANT_RESET_MOBILE_KEY + phone, 60 * 3, uuid);

		return Results.success(uuid);
	}

	@Override
	public Result<String> checkUser(String phone, String userName) {
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByLoginNameAndPhone(userName, phone);
		if (user == null) {
			return Results.failure("用户不存在!");
		}
		return Results.success();
	}

	@Override
	public Result<String> selectUser(String phone, String userName) {
		if (StringUtil.isEmpty(phone) || StringUtil.isEmpty(userName)) {
			return Results.failure("用户不存在!");
		}
		Inno72MerchantUser user = inno72MerchantUserMapper.selectByMerchantName(userName);
		if (user == null) {
			return Results.failure("商家不存在!");
		}
		if (user.getPhone().equals(phone)) {
			return Results.success();
		}

		return Results.failure("商家预留手机号不匹配!");
	}
}
