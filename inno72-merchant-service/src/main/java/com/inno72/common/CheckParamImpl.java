package com.inno72.common;

import javax.annotation.Resource;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.inno72.redis.IRedisUtil;

@Aspect
@Component
public class CheckParamImpl {
	private static final Logger logger = LoggerFactory.getLogger(CheckParamImpl.class);

	@Resource
	private IRedisUtil redisUtil;

	@Pointcut("@annotation(com.inno72.common.CheckParams)")
	public void codeRequired() {
	}

	@Around("codeRequired()")
	public Object around(ProceedingJoinPoint proceedingJoinPoint) {

		try {

			Object[] args = proceedingJoinPoint.getArgs();
			for (Object o : args) {
				if (o == null) {
					logger.info("参数{}为空", o);
					return Results.failure("参数缺失!");
				}
			}

			Object proceed = proceedingJoinPoint.proceed();
			return proceed;
		} catch (Throwable throwable) {
			throwable.printStackTrace();
		}

		return Results.failure("请求失败!");
	}

}
