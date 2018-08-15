package com.inno72.system.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.system.mapper.Inno72UserMapper;
import com.inno72.system.mapper.Inno72UserRoleMapper;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserRole;
import com.inno72.system.service.UserService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@Service
@Transactional
public class UserServiceImpl extends AbstractService<Inno72User> implements UserService {
	@Resource
	private Inno72UserMapper inno72UserMapper;
	@Resource
	private Inno72UserRoleMapper inno72UserRoleMapper;

	@Override
	public Result<Inno72User> getUserByUserId(String userId) {
		Condition condition = new Condition(Inno72User.class);
		condition.createCriteria().andEqualTo("userId", userId).andEqualTo("isDelete", 0);
		List<Inno72User> users = inno72UserMapper.selectByCondition(condition);
		if (users != null && users.size() != 0) {
			return ResultGenerator.genSuccessResult(users.get(0));
		}
		return ResultGenerator.genSuccessResult(null);

	}

	@Override
	public Result<List<Inno72User>> findUsers(String keyword) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		List<Inno72User> users = inno72UserMapper.selectUsersByPage(param);
		return Results.success(users);
	}

	@Override
	public Result<String> auth(String userId, String roleIds) {
		try {
			JSONArray array = JSON.parseArray(roleIds);
			if (array.isEmpty()) {
				return Results.failure("角色参数错误");
			}
			Condition condition = new Condition(Inno72UserRole.class);
			condition.createCriteria().andEqualTo("userId", userId);
			inno72UserRoleMapper.deleteByCondition(condition);
			array.forEach(role -> {
				Inno72UserRole ur = new Inno72UserRole();
				ur.setId(StringUtil.getUUID());
				ur.setRoleId(role.toString());
				ur.setUserId(userId);
				inno72UserRoleMapper.insert(ur);
			});

		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("角色参数错误");
		}
		return Results.success();
	}

	@Override
	public Result<List<Inno72UserRole>> queryUserRoles(String userId) {
		Condition condition = new Condition(Inno72UserRole.class);
		condition.createCriteria().andEqualTo("userId", userId);
		List<Inno72UserRole> urs = inno72UserRoleMapper.selectByCondition(condition);
		return Results.success(urs);
	}

}
