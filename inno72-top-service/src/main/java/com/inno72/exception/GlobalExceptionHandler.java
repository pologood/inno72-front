package com.inno72.exception;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import com.taobao.api.ApiException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * 全局异常处理
 */
@ControllerAdvice
public class GlobalExceptionHandler {
	public static final String CODE = "inno72-top-error";

	@ExceptionHandler(value = ApiException.class)
	@ResponseBody
	public Map<String, String> apiExceptionHandler(HttpServletRequest req, Exception e) {
		Map<String, String> re = new HashMap<String, String>();
		re.put("code", CODE);
		re.put("msg", e.getMessage());
		return re;
	}

	@ExceptionHandler(value = ParameterException.class)
	@ResponseBody
	public Map<String, String> parameterExceptionHandler(HttpServletRequest req, Exception e) {
		Map<String, String> re = new HashMap<String, String>();
		re.put("code", CODE);
		re.put("msg", e.getMessage());
		return re;
	}

	@ExceptionHandler(value = Exception.class)
	@ResponseBody
	public Map<String, String> exceptionHandler(HttpServletRequest req, Exception e) {
		Map<String, String> re = new HashMap<String, String>();
		re.put("code", CODE);
		re.put("msg", e.getMessage());
		return re;
	}

}