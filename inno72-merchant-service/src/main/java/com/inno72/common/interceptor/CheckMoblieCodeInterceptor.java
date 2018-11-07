package com.inno72.common.interceptor;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonBean;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.redis.IRedisUtil;

public class CheckMoblieCodeInterceptor implements HandlerInterceptor {

	private static final Logger logger = LoggerFactory.getLogger(CheckMoblieCodeInterceptor.class);

	private static List<String> doNotCheckUs =Arrays.asList("/inno72/merchant/resetPhone","/inno72/merchant/resetPhone");



	@Resource
	private IRedisUtil redisUtil;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		if (doNotCheckUs.parallelStream().anyMatch(request.getServletPath()::contains)){
			String code = request.getParameter("code");
			String phone = request.getParameter("phone");
			logger.info("验证信息 {}, {}", phone, code);
			if (StringUtil.isEmpty(code) || StringUtil.isEmpty(phone)){
				response.getWriter().println(JSON.toJSONString(Results.failure("验证码失效!")));
				response.getWriter().flush();
				response.getWriter().close();
				return false;
			}
			String cacheCode = redisUtil.get(CommonBean.REDIS_MERCHANT_MOBILE_CODE_RESET_PWD + phone);
			if (StringUtil.isEmpty(cacheCode)){
				response.getWriter().println(JSON.toJSONString(Results.failure("验证码错误!")));
				response.getWriter().flush();
				response.getWriter().close();
				return false;
			}
		}
		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {

	}

	public IRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}
}
