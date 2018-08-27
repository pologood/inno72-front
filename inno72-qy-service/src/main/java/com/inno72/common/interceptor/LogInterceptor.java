package com.inno72.common.interceptor;

import com.alibaba.fastjson.JSON;
import com.inno72.common.CommonConstants;
import com.inno72.common.Result;
import com.inno72.common.SessionData;
import com.inno72.common.StringUtil;
import com.inno72.redis.IRedisUtil;
import com.inno72.utils.page.Pagination;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.*;

/**
 * 项目拦截器
 * 
 * @author lzh
 *
 */
public class LogInterceptor extends HandlerInterceptorAdapter {

	@Resource
	private IRedisUtil redisUtil; // memcachedClient

	private static List<String> doNotCheckUs = Arrays.asList("/check/user/smsCode","/check/user/login",
			"/check/user/operation","/app/version/find","/main");

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

		// 检查POST方法，token，url权限, 启用后删除检查参数中的token
		 boolean flag = checkAuthority(request,response);
		 if(!flag){
		     return false;
         }
		@SuppressWarnings("rawtypes")
		Enumeration enumeration = request.getParameterNames();
        StringBuilder parm = new StringBuilder();
		while (enumeration.hasMoreElements()) {
			Object element = enumeration.nextElement();
			if (element instanceof String) {
				String name = (String) element;
				Object attr = request.getParameter(name);
				boolean isv = false;
				if (name.equals("v") || name.equals("V")) {
					isv = true;
				}
				if (!isv) {
					parm.append(name).append("=").append(attr).append("&");

					String attrStr = (String) attr;

					if (name.equals("pageNo")) {
						Pagination pagination = new Pagination();
						int pageNo = 1;
						try {
							if (attrStr.contains("_")) {
								pageNo = Integer.parseInt(attrStr.split("_")[0]);
								pagination.setPageSize(Integer.parseInt(attrStr.split("_")[1]));
							} else {
								pageNo = Integer.parseInt(attrStr);
							}
							pagination.setPageNo(pageNo);
						} catch (Exception ignored) {
						}
						if(pageNo<1){
						    pageNo = 1;
                        }
						pagination.setPageNo(pageNo);
						Pagination.threadLocal.set(pagination);
					}
				}

			}
		}

		return true;

	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) {

		// 获取controller返回值

		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();
			Map<String, Object> newModel = new HashMap<>();
			for (Map.Entry<String, Object> item : model.entrySet()) {
				Object attr = item.getValue();
				// 把所有值为空的key变为""
				if (attr == null) {
					newModel.put(item.getKey(), "");
				}

			}
			modelAndView.addAllObjects(newModel);

		}

		log(request, modelAndView);

	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
	}

	private void log(HttpServletRequest request, ModelAndView modelAndView) {

		Map<String, Object> map = new HashMap<>();

		// 请求参数
		map.put("request_data", request.getParameterMap());

		// 响应结果
		Map<String, Object> response_data = new HashMap<>();
		if (modelAndView != null) {
			Map<String, Object> model = modelAndView.getModel();

			model.forEach((key, attr) -> {

                if ("result".equals(key)) {
                    response_data.put(key, JSON.toJSONString(attr));
                }
                if ("data".equals(key)) {
                    response_data.put(key, JSON.toJSONString(attr));
                }
                if ("code".equals(key)) {
                    response_data.put(key, JSON.toJSONString(attr));
                }
            });
		}
		map.put("response_data", response_data);

		Map<String, Object> _log = new HashMap<>();
		_log.put(request.getRequestURI(), map);

    }

	@SuppressWarnings("unused")
	private boolean checkAuthority(HttpServletRequest request, HttpServletResponse response) {
		response.reset();
		String url = request.getServletPath();
		boolean match = doNotCheckUs.parallelStream().anyMatch(url::contains);
		if (match) {
			return true;
		}
		// 获取请求方法
		String requestMethod = request.getMethod().toUpperCase();
		if (requestMethod.equals("GET") || requestMethod.equals("POST") || requestMethod.equals("DELETE")
				|| requestMethod.equals("PUT")) {
				// lf-None-Matoh 传入token
            String token = request.getHeader("lf-None-Matoh");
            if (StringUtil.isEmpty(token)) {
                Result<String> result = new Result<>();
                result.setCode(999);
                result.setMsg("你未登录，请登录");
                String origin = request.getHeader("Origin");
                response(response, origin);
                try (PrintWriter out = response.getWriter()) {
                    out.append(JSON.toJSONString(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            }
            String info = redisUtil.get(CommonConstants.USER_LOGIN_CACHE_KEY_PREF + token);
            if (info == null) {
                Result<String> result = new Result<>();
				result.setCode(999);
				result.setMsg("你登录超时，请重新登录");
                String origin = request.getHeader("Origin");
                response(response, origin);
                try (PrintWriter out = response.getWriter()) {
                    out.append(JSON.toJSONString(result));
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return false;
            } else {
                CommonConstants.SESSION_DATA = JSON.parseObject(info, SessionData.class);
            }
		}
		return true;
	}

	private void response(HttpServletResponse response, String origin) {
		response.setCharacterEncoding("UTF-8");
		response.setContentType("application/json; charset=utf-8");
		response.setHeader("Access-Control-Allow-Origin", origin);
		response.setHeader("Vary", "Origin");
		// Access-Control-Max-Age
		response.setHeader("Access-Control-Max-Age", "3600");
		// Access-Control-Allow-Credentials
		response.setHeader("Access-Control-Allow-Credentials", "true");
		// Access-Control-Allow-Methods
		response.setHeader("Access-Control-Allow-Methods", "GET, POST, DELETE, PUT, HEAD, OPTIONS");
		// Access-Control-Allow-Headers
		response.setHeader("Access-Control-Allow-Headers",
				"Origin, X-Requested-With, Content-Type, Accept, lf-None-Matoh");
	}

	public IRedisUtil getRedisUtil() {
		return redisUtil;
	}

	public void setRedisUtil(IRedisUtil redisUtil) {
		this.redisUtil = redisUtil;
	}

}
