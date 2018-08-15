package com.inno72.system.service.impl;

import java.text.MessageFormat;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.inno72.common.CommonConstants;
import com.inno72.common.Inno72AlarmServiceProperties;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.SessionData;
import com.inno72.common.json.JsonUtil;
import com.inno72.common.utils.StringUtil;
import com.inno72.plugin.http.HttpClient;
import com.inno72.redis.IRedisUtil;
import com.inno72.system.encrypt.DingTalkEncryptException;
import com.inno72.system.encrypt.DingTalkEncryptor;
import com.inno72.system.model.Inno72Dept;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserDept;
import com.inno72.system.service.DDService;
import com.inno72.system.service.DeptService;
import com.inno72.system.service.FunctionService;
import com.inno72.system.service.UserDeptService;
import com.inno72.system.service.UserService;
import com.inno72.system.vo.UserDeptVo;

import tk.mybatis.mapper.entity.Condition;

@Service
public class DDServiceImpl implements DDService {
	Logger logger = LoggerFactory.getLogger(DDServiceImpl.class);
	@Autowired
	private DeptService deptService;
	@Autowired
	private UserService userService;
	@Autowired
	private UserDeptService userDeptService;
	@Autowired
	private FunctionService functionService;
	@Autowired
	private IRedisUtil redisUtil;
	@Autowired
	private Inno72AlarmServiceProperties inno72AlarmServiceProperties;
	// 需要写在配置中心
	// String appid = "dingoa25um8bzdtan7hjgw";
	// String appsecret =
	// "z3ZGL5THRX-qW-dwKi7vrBWNmnKUcSo3R5eLoPK2hA5SR4ITEDtZ_MhD7D5zHf4G";
	// String callback = "http://47.95.217.215:30901/dd";

	@Override
	public String process(String data, String signature, String timestamp, String nonce) {

		JSONObject jsonEncrypt = JSONObject.parseObject(data);
		String encrypt = jsonEncrypt.getString("encrypt");
		DingTalkEncryptor dingTalkEncryptor = null;
		String plainText = null;

		try {
			dingTalkEncryptor = new DingTalkEncryptor(CommonConstants.DD_TOKEN, CommonConstants.AES_KEY,
					CommonConstants.DD_CORPID);
			plainText = dingTalkEncryptor.getDecryptMsg(signature, timestamp, nonce, encrypt);
		} catch (DingTalkEncryptException e) {
			logger.info(e.getMessage());
			e.printStackTrace();
		}
		JSONObject plainTextJson = JSONObject.parseObject(plainText);
		String eventType = plainTextJson.getString("EventType");
		logger.info(plainText);
		String result = null;

		switch (eventType) {
		case "user_add_org":// 新增用户
			logger.info("钉钉新增用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				UserDeptVo userVo = buidUserById(userId.toString());
				if (user_db == null) {
					Inno72User user = userVo.getUser();
					user.setId(StringUtil.getUUID());
					userService.save(user);

					userVo.getDeptIds().forEach(dept -> {
						Inno72UserDept ud = new Inno72UserDept();
						ud.setId(StringUtil.getUUID());
						ud.setDeptId(dept.toString());
						ud.setUserId(user.getId());
						userDeptService.save(ud);
					});
				}

			});
			result = "success";
			break;
		case "user_modify_org":// 修改用户
			logger.info("钉钉修改用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				UserDeptVo userVo = buidUserById(userId.toString());
				if (user_db != null) {

					Inno72User user = userVo.getUser();
					user.setId(user_db.getId());
					userService.update(user);

					userDeptService.deleteByUserId(user_db.getId());
					userVo.getDeptIds().forEach(dept -> {
						Inno72UserDept ud = new Inno72UserDept();
						ud.setId(StringUtil.getUUID());
						ud.setDeptId(dept.toString());
						ud.setUserId(user.getId());
						userDeptService.save(ud);
					});
				}

			});
			result = "success";
			break;
		case "user_leave_org":// 用户离职
			logger.info("钉钉离职用户回调");
			plainTextJson.getJSONArray("UserId").forEach(userId -> {
				Result<Inno72User> userResult = userService.getUserByUserId(userId.toString());
				Inno72User user_db = userResult.getData();
				if (user_db != null) {
					user_db.setIsDelete(1);
					userService.update(user_db);
					userDeptService.deleteByUserId(user_db.getId());
				}

			});
			result = "success";
			break;

		case "org_dept_create":// 创建部门
			logger.info("钉钉新增部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptId -> {
				Inno72Dept dept = buidDeptById(deptId.toString());
				deptService.save(dept);
			});
			result = "success";
			break;

		case "org_dept_modify":// 修改部门
			logger.info("钉钉修改部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptId -> {
				deptService.deleteById(deptId.toString());
				Inno72Dept dept = buidDeptById(deptId.toString());
				deptService.save(dept);
			});
			result = "success";
			break;

		case "org_dept_remove":// 删除部门
			logger.info("钉钉删除部门回调");
			plainTextJson.getJSONArray("DeptId").forEach(deptid -> {
				deptService.deleteById(deptid.toString());
			});
			result = "success";
			break;
		case "check_url":// 回调接口注册验证
			result = "success";
		default: // do something
			result = "success";
			break;
		}
		long timeStampLong = Long.parseLong(timestamp);
		Map<String, String> jsonMap = null;
		try {
			jsonMap = dingTalkEncryptor.getEncryptedMap(result, timeStampLong, nonce);
		} catch (DingTalkEncryptException e) {
			e.printStackTrace();
		}
		JSONObject json = new JSONObject();
		json.putAll(jsonMap);
		return json.toString();
	}

	@Override
	public Result<SessionData> login(String code, String state) {
		Result<String> tokenResult = getLoginToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return Results.failure("获取token失败");
		}
		if (!StringUtil.isEmpty(state)) {
			Result<String> persisintResult = getPersisintCode(code, tokenResult.getData());
			if (persisintResult.getCode() != Result.SUCCESS) {
				return Results.failure("获取授权码失败");
			}
			Result<String> snsTokenResult = getSnsToken(persisintResult.getData(), tokenResult.getData());
			if (snsTokenResult.getCode() != Result.SUCCESS) {
				return Results.failure("获取snsToken失败");
			}
			Result<String> dingIdResult = getDingId(snsTokenResult.getData());
			if (dingIdResult.getCode() != Result.SUCCESS) {
				return Results.failure("获取dingId失败");
			}
			// 历经九九八十一难终于拿到用户的一个标识
			Inno72User user = userService.findBy("dingId", dingIdResult.getData());
			// List<Inno72Function> functions = functionService.findAll();
			List<Inno72Function> functions = functionService.findFunctionsByUserId(user.getId());
			String token = StringUtil.getUUID();
			SessionData sessionData = new SessionData(token, user, functions);
			// 获取用户token使用
			String userTokenKey = CommonConstants.USER_LOGIN_TOKEN_CACHE_KEY_PREF + user.getId();
			// 获取用户之前登录的token
			String oldToken = redisUtil.get(userTokenKey);
			// 清除之前的登录信息
			if (StringUtil.isNotBlank(oldToken)) {
				redisUtil.del(CommonConstants.USER_LOGIN_CACHE_KEY_PREF + oldToken);
				// 记录被踢出
				redisUtil.sadd(CommonConstants.CHECK_OUT_USER_TOKEN_SET_KEY, oldToken);
			}
			// 保存新登录的token
			redisUtil.set(userTokenKey, token);
			// 用户登录信息缓存
			String userInfoKey = CommonConstants.USER_LOGIN_CACHE_KEY_PREF + token;
			// 缓存用户登录sessionData
			redisUtil.set(userInfoKey, JsonUtil.toJson(sessionData));
			redisUtil.expire(userInfoKey, CommonConstants.SESSION_DATA_EXP);
			redisUtil.expire(userTokenKey, CommonConstants.SESSION_DATA_EXP);
			return Results.success(sessionData);
		}
		return Results.success();
	}

	private Result<String> getLoginToken() {
		String appid = inno72AlarmServiceProperties.get("ddAppid");
		String appsecret = inno72AlarmServiceProperties.get("ddAppsecret");
		String url = MessageFormat.format("https://oapi.dingtalk.com/sns/gettoken?appid={0}&appsecret={1}", appid,
				appsecret);
		String result = HttpClient.get(url);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(resultJson.getString("access_token"));
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	private Result<String> getPersisintCode(String tmpAuthCode, String token) {
		String url = MessageFormat.format("https://oapi.dingtalk.com/sns/get_persistent_code?access_token={0}", token);
		JSONObject json = new JSONObject();
		json.put("tmp_auth_code", tmpAuthCode);
		String result = HttpClient.post(url, json.toJSONString());
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(result);
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	private Result<String> getSnsToken(String data, String token) {
		String url = MessageFormat.format("https://oapi.dingtalk.com/sns/get_sns_token?access_token={0}", token);
		String result = HttpClient.post(url, data);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(resultJson.getString("sns_token"));
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	private Result<String> getDingId(String snsToken) {
		String url = MessageFormat.format("https://oapi.dingtalk.com/sns/getuserinfo?sns_token={0}", snsToken);
		String result = HttpClient.get(url);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(resultJson.getJSONObject("user_info").getString("dingId"));
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	@Override
	public Result<String> getToken() {
		String url = "https://oapi.dingtalk.com/gettoken?corpid=dingd04d2d6ca18d0fd535c2f4657eb6378f&corpsecret=2ralypy62nV4kL8DOMjWWEoJyQkFnjNhlin3PzdkIMs1LQ7jj8huTsqibi7UdaKD";
		String result = HttpClient.get(url);
		JSONObject resultJson = JSON.parseObject(result);
		if (resultJson.getInteger("errcode") == 0) {
			return ResultGenerator.genSuccessResult(resultJson.getString("access_token"));
		}
		return ResultGenerator.genFailResult(resultJson.getString("errmsg"));
	}

	@Override
	public Result<String> registryCallback(String url) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		String api = "https://oapi.dingtalk.com/call_back/register_call_back?access_token=" + token;
		String[] tags = { "user_add_org", "user_modify_org", "user_leave_org" };
		JSONObject json = new JSONObject();
		json.put("call_back_tag", tags);
		json.put("token", CommonConstants.DD_TOKEN);
		json.put("aes_key", CommonConstants.AES_KEY);
		json.put("url", url);

		String $j = HttpClient.post(api, json.toJSONString());
		JSONObject $json = JSON.parseObject($j);
		String errcode = $json.getString("errcode");
		if (!errcode.equals("0")) {
			return ResultGenerator.genFailResult($json.getString("errmsg"));
		}
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> updateRegistryCallback(String url) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		String[] tags = { "user_add_org", "user_modify_org", "user_leave_org", "org_dept_create", "org_dept_modify",
				"org_dept_remove" };
		String api = "https://oapi.dingtalk.com/call_back/update_call_back?access_token=" + token;
		JSONObject json = new JSONObject();
		json.put("call_back_tag", tags);
		json.put("token", CommonConstants.DD_TOKEN);
		json.put("aes_key", CommonConstants.AES_KEY);
		json.put("url", url);

		String $j = HttpClient.post(api, json.toJSONString());
		JSONObject $json = JSON.parseObject($j);
		String errcode = $json.getString("errcode");
		if (!errcode.equals("0")) {
			return ResultGenerator.genFailResult($json.getString("errmsg"));
		}
		return ResultGenerator.genSuccessResult();

	}

	@Override
	public Result<String> initDData() {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return ResultGenerator.genFailResult("获取token失败");
		}
		String token = tokenResult.getData();
		// 初始化部门数据 先删除所有部门
		deptService.deleteAll();
		userDeptService.deleteAll();
		String deptUrl = MessageFormat.format("https://oapi.dingtalk.com/department/list?access_token={0}", token);
		String dept_result = HttpClient.get(deptUrl);
		JSONObject deptJson = JSON.parseObject(dept_result);
		JSONArray dept_ary = deptJson.getJSONArray("department");
		dept_ary.forEach(dept -> {
			JSONObject dept_info = JSON.parseObject(dept.toString());
			String deptId = dept_info.getString("id");
			Inno72Dept inno72Dept = buidDeptById(deptId);
			if (inno72Dept != null) {
				deptService.save(inno72Dept);
			}
			// 初始化用户
			initUser(deptId, token);
		});
		return ResultGenerator.genSuccessResult();
	}

	private void initUser(String deptId, String token) {
		String dept_user_url = MessageFormat
				.format("https://oapi.dingtalk.com/user/list?access_token={0}&department_id={1}", token, deptId);
		String dept_user_result = HttpClient.get(dept_user_url);
		JSONObject dept_user_json = JSON.parseObject(dept_user_result);
		JSONArray user_ary = dept_user_json.getJSONArray("userlist");
		user_ary.forEach(user_info -> {
			// 解析用户数据
			JSONObject $user = JSON.parseObject(user_info.toString());
			// 根据userid查询本地库,有数据为修改事件，没数据为新增事件
			Result<Inno72User> userResult = userService.getUserByUserId($user.getString("userid"));
			Inno72User user = userResult.getData();
			if (user == null) {
				user = new Inno72User();
				String id = StringUtil.getUUID();
				user.setId(id);
				user.setAvatar($user.getString("avatar"));
				user.setCreateTime(LocalDateTime.now());
				user.setEmail($user.getString("email"));
				user.setMobile($user.getString("mobile"));
				user.setName($user.getString("name"));
				user.setOrgEmail($user.getString("orgEmail"));
				user.setPosition($user.getString("position"));
				user.setUserId($user.getString("userid"));
				user.setDingId($user.getString("dingId"));
				user.setIsDelete(0);
				userService.save(user);

			}
			Inno72UserDept ud = new Inno72UserDept();
			ud.setId(StringUtil.getUUID());
			ud.setDeptId(deptId);
			ud.setUserId(user.getId());
			userDeptService.save(ud);
		});
	}

	public Inno72Dept buidDeptById(String deptId) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return null;
		}
		String token = tokenResult.getData();
		String deptDetailUrl = MessageFormat.format("https://oapi.dingtalk.com/department/get?access_token={0}&id={1}",
				token, deptId);
		String json = HttpClient.get(deptDetailUrl);
		JSONObject $json = JSON.parseObject(json);
		Inno72Dept inno72Dept = new Inno72Dept();
		inno72Dept.setId($json.getString("id"));
		inno72Dept.setName($json.getString("name"));
		inno72Dept.setSeq($json.getInteger("order"));
		inno72Dept.setParentId($json.getString("parentid"));
		return inno72Dept;
	}

	public UserDeptVo buidUserById(String userId) {
		Result<String> tokenResult = getToken();
		if (tokenResult.getCode() != Result.SUCCESS) {
			return null;
		}
		String token = tokenResult.getData();
		String userDetailUrl = MessageFormat.format("https://oapi.dingtalk.com/user/get?access_token={0}&userid={1}",
				token, userId);
		String json = HttpClient.get(userDetailUrl);
		JSONObject $user = JSON.parseObject(json);
		Inno72User user = new Inno72User();
		user.setAvatar($user.getString("avatar"));
		user.setCreateTime(LocalDateTime.now());
		user.setEmail($user.getString("email"));
		user.setMobile($user.getString("mobile"));
		user.setName($user.getString("name"));
		user.setOrgEmail($user.getString("orgEmail"));
		user.setPosition($user.getString("position"));
		user.setUserId($user.getString("userid"));
		user.setDingId($user.getString("dingId"));
		user.setIsDelete(0);
		UserDeptVo vo = new UserDeptVo();
		vo.setUser(user);
		vo.setDeptIds($user.getJSONArray("department"));
		return vo;
	}

	@Override
	public Result<SessionData> testLogin(String phone, String name) {
		Condition condition = new Condition(Inno72User.class);
		condition.createCriteria().andEqualTo("mobile", phone).andEqualTo("name", name);
		List<Inno72User> users = userService.findByCondition(condition);
		if (users == null || users.size() != 1) {
			return Results.failure("登录失败");
		}
		Inno72User user = users.get(0);
		List<Inno72Function> functions = functionService.findAll();
		// List<Inno72Function> functions =
		// functionService.findFunctionsByUserId(user.getId());
		String token = StringUtil.getUUID();
		SessionData sessionData = new SessionData(token, user, functions);
		// 获取用户token使用
		String userTokenKey = CommonConstants.USER_LOGIN_TOKEN_CACHE_KEY_PREF + user.getId();
		// 获取用户之前登录的token
		String oldToken = redisUtil.get(userTokenKey);
		// 清除之前的登录信息
		if (StringUtil.isNotBlank(oldToken)) {
			redisUtil.del(CommonConstants.USER_LOGIN_CACHE_KEY_PREF + oldToken);
			// 记录被踢出
			redisUtil.sadd(CommonConstants.CHECK_OUT_USER_TOKEN_SET_KEY, oldToken);
		}
		// 保存新登录的token
		redisUtil.set(userTokenKey, token);
		// 用户登录信息缓存
		String userInfoKey = CommonConstants.USER_LOGIN_CACHE_KEY_PREF + token;
		// 缓存用户登录sessionData
		redisUtil.set(userInfoKey, JsonUtil.toJson(sessionData));
		redisUtil.expire(userInfoKey, CommonConstants.SESSION_DATA_EXP);
		redisUtil.expire(userTokenKey, CommonConstants.SESSION_DATA_EXP);
		return Results.success(sessionData);
	}

}
