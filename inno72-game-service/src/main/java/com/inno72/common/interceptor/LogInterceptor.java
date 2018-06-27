//package com.inno72.common.interceptor;
//
//import java.util.Arrays;
//import java.util.Enumeration;
//import java.util.HashMap;
//import java.util.List;
//import java.util.Map;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//
//import org.springframework.web.HttpRequestMethodNotSupportedException;
//import org.springframework.web.servlet.ModelAndView;
//import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
//
//import com.alibaba.fastjson.JSON;
//import com.framelib.common.CommonConstants;
//import com.framelib.common.GeneralAuthority;
//import com.framelib.common.PmpSessionData;
//import com.framelib.exception.SystemException;
//import com.inno72.redis.IRedisUtil;
//import com.inno72.utils.page.Pagination;
//
///**
// * Title:LogInterceptor Description:记录用户日志
// *
// * @Create_by:yinsy
// * @Create_date:2016年3月25日
// * @Last_Edit_By:
// * @Edit_Description:
// * @version:heima-service 1.0
// */
//public class LogInterceptor extends HandlerInterceptorAdapter {
//
//	@Resource
//	private IRedisUtil redisUtil; // memcachedClient
//
//	private static List<String> doNotCheckUs = Arrays.asList(new String[] { "/login.json"});
//
//	@Override
//	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
//			throws Exception {
//
//		// 检查POST方法，token，url权限, 启用后删除检查参数中的token
////		checkAuthority(request);
//
//		@SuppressWarnings("rawtypes")
//		Enumeration enumeration = request.getParameterNames();
//		StringBuffer parm = new StringBuffer();
//
//		// 移除分页对象
//		Pagination.threadLocal.remove();
//		while (enumeration.hasMoreElements()) {
//			Object element = enumeration.nextElement();
//			if (element instanceof String) {
//				String name = (String) element;
//				Object attr = request.getParameter(name);
//				boolean isv = false;
//				if (name.equals("v") || name.equals("V")) {
//					isv = true;
//				}
//				if (!isv) {
//					parm.append(name).append("=").append(attr).append("&");
//
//					String attrStr = (String) attr;
//
//					if (name.equals("pageNo")) {
//						Pagination pagination = new Pagination();
//						int pageNo = 1;
//						try {
//							if (attrStr.indexOf("_") != -1) {
//								pageNo = Integer.parseInt(attrStr.split("_")[0]);
//								pagination.setPageSize(Integer.parseInt(attrStr.split("_")[1]));
//							} else {
//								pageNo = Integer.parseInt(attrStr);
//							}
//							pagination.setPageNo(pageNo);
//						} catch (Exception e) {
//						}
//						pagination.setPageNo(pageNo);
//						Pagination.threadLocal.set(pagination);
//					}
//				}
//
//			}
//		}
//
//		return true;
//
//	}
//
//	@Override
//	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
//			ModelAndView modelAndView) throws Exception {
//
//		// 获取controller返回值
//
//		if (modelAndView != null) {
//			Map<String, Object> model = modelAndView.getModel();
//			Map<String, Object> newModel = new HashMap<String, Object>();
//			for (Map.Entry<String, Object> item : model.entrySet()) {
//				Object attr = item.getValue();
//				// 把所有值为空的key变为""
//				if (attr == null) {
//					newModel.put(item.getKey(), "");
//				}
//
//			}
//			modelAndView.addAllObjects(newModel);
//
//		}
//
//		log(request, response, modelAndView);
//
//	}
//
//	@Override
//	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
//			throws Exception {
//	}
//
//	private String log(HttpServletRequest request, HttpServletResponse response, ModelAndView modelAndView) {
//
//		Map<String, Object> map = new HashMap<>();
//
//		// 请求参数
//		map.put("request_data", request.getParameterMap());
//
//		// 响应结果
//		Map<String, Object> response_data = new HashMap<String, Object>();
//		if (modelAndView != null) {
//			Map<String, Object> model = modelAndView.getModel();
//
//			model.entrySet().forEach(entry -> {
//				Object attr = entry.getValue();
//				String key = entry.getKey();
//
//				if ("result".equals(key)) {
//					response_data.put(key, JSON.toJSONString(attr));
//				}
//				if ("data".equals(key)) {
//					response_data.put(key, JSON.toJSONString(attr));
//				}
//				if ("code".equals(key)) {
//					response_data.put(key, JSON.toJSONString(attr));
//				}
//			});
//		}
//		map.put("response_data", response_data);
//
//		Map<String, Object> _log = new HashMap<>();
//		_log.put(request.getRequestURI(), map);
//
//		// log.log(_log, "gmp_info", request.getRequestURI(), "gmp");
//
//		return null;
//	}
//
//	private void checkAuthority(HttpServletRequest request) throws HttpRequestMethodNotSupportedException {
//		String url = request.getServletPath();
//		boolean match = doNotCheckUs.parallelStream().anyMatch(_url -> url.indexOf(_url) != -1);
//		if (match) {
//			return;
//		}
//		if (request.getMethod().toUpperCase().equals("GET") || request.getMethod().toUpperCase().equals("POST")) {
//			if (!match) {
//				// lf-None-Matoh 传入token
//				String token = request.getHeader("lf-None-Matoh");
//				if (token == null) {
//					throw new SystemException("sessionOverdue");
//				}
////				Object info = clientMemcached.getAndTouch(token, CommonConstants.PMP_SESSION_DATA_EXP);
//				Object info = new Object();
//				if (info == null) {
//					throw new SystemException("sessionOverdue");
//				} else {
//					String _info = info.toString();
//					CommonConstants.PMP_SESSION_DATA = this.toSessionData(_info);
//					List<GeneralAuthority> authorities = CommonConstants.PMP_SESSION_DATA.getThirdLevelAuth();
//					boolean isAuth = authorities.parallelStream().anyMatch(
//							auth -> url.indexOf(auth.getDetail() == null ? "detail字段补全" : auth.getDetail()) != -1);
//					if (!isAuth) {
//						// throw new SystemException("permission.denied",
//						// "没有权限");
//					}
//				}
//			}
//
//		}
//
//	}
//
//	public static PmpSessionData toSessionData(String jsonSessionData) {
//		return JSON.parseObject(jsonSessionData, PmpSessionData.class);
//	}
//
//	public IRedisUtil getRedisUtil() {
//		return redisUtil;
//	}
//
//	public void setRedisUtil(IRedisUtil redisUtil) {
//		this.redisUtil = redisUtil;
//	}
//
//	
//}
