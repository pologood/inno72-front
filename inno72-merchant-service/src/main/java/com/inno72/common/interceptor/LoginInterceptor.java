package com.inno72.common.interceptor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonBean;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.redis.IRedisUtil;

public class LoginInterceptor extends HandlerInterceptorAdapter {

	private static final Logger LOGGER = LoggerFactory.getLogger(LoggerFactory.class);

	private static List<String> doNotCheckUs =Arrays.asList("/inno72/merchant/login");

	@Resource
	private IRedisUtil redisUtil;
	/**
	 * This implementation always returns {@code true}.
	 */
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (doNotCheckUs.parallelStream().anyMatch(request.getServletPath()::contains)){
			return true;
		}

		String token = request.getHeader("lf-None-Matoh");

		if (StringUtil.isEmpty(token)){
			LOGGER.info("非法请求 {} ", request);
			return false;
		}

		String session = redisUtil.get(CommonBean.REDIS_MERCHANT_LOGIN_SESSION_KEY + token);

		if (StringUtil.isEmpty(session)){
			LOGGER.info("未登录请求 {} ", request);
			return false;
		}

		redisUtil.setex(CommonBean.REDIS_MERCHANT_LOGIN_SESSION_KEY + token,
				CommonBean.REDIS_MERCHANT_LOGIN_SESSION_KEY_TIMES,
				session);

		return true;
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void postHandle(
			HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterCompletion(
			HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
	}

	/**
	 * This implementation is empty.
	 */
	@Override
	public void afterConcurrentHandlingStarted(
			HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
	}
	public IRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
}
