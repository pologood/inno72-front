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
import com.inno72.common.Results;
import com.inno72.common.utils.StringUtil;
import com.inno72.system.mapper.Inno72RoleFunctionMapper;
import com.inno72.system.mapper.Inno72RoleMapper;
import com.inno72.system.mapper.Inno72UserRoleMapper;
import com.inno72.system.model.Inno72Role;
import com.inno72.system.model.Inno72RoleFunction;
import com.inno72.system.model.Inno72UserRole;
import com.inno72.system.service.RoleService;

import tk.mybatis.mapper.entity.Condition;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
@Service
@Transactional
public class RoleServiceImpl extends AbstractService<Inno72Role> implements RoleService {
	@Resource
	private Inno72RoleMapper inno72RoleMapper;
	@Resource
	private Inno72RoleFunctionMapper inno72RoleFunctionMapper;
	@Resource
	private Inno72UserRoleMapper inno72UserRoleMapper;

	@Override
	public Result<List<Inno72Role>> findRoles(String keyword) {
		Map<String, Object> param = new HashMap<>();
		param.put("keyword", keyword);
		List<Inno72Role> roles = inno72RoleMapper.selectRolesByPage(param);
		return Results.success(roles);
	}

	@Override
	public Result<String> add(String name, String auths) {
		try {
			JSONArray array = JSON.parseArray(auths);
			String roleId = StringUtil.getUUID();
			array.forEach(auth -> {
				Inno72RoleFunction rf = new Inno72RoleFunction();
				rf.setId(StringUtil.getUUID());
				rf.setFunctionId(auth.toString());
				rf.setRoleId(roleId);
				inno72RoleFunctionMapper.insert(rf);
			});
			Inno72Role role = new Inno72Role();
			role.setId(roleId);
			role.setName(name);
			inno72RoleMapper.insert(role);

		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("权限参数错误");
		}
		return Results.success();
	}

	@Override
	public Result<String> update(String id, String name, String auths) {
		Inno72Role role = inno72RoleMapper.selectByPrimaryKey(id);
		if (role == null) {
			return Results.failure("角色id错误");
		}
		try {
			JSONArray array = JSON.parseArray(auths);
			Condition condition = new Condition(Inno72RoleFunction.class);
			condition.createCriteria().andEqualTo("roleId", id);
			inno72RoleFunctionMapper.deleteByCondition(condition);
			array.forEach(auth -> {
				Inno72RoleFunction rf = new Inno72RoleFunction();
				rf.setId(StringUtil.getUUID());
				rf.setFunctionId(auth.toString());
				rf.setRoleId(role.getId());
				inno72RoleFunctionMapper.insert(rf);
			});
			role.setName(name);
			inno72RoleMapper.updateByPrimaryKeySelective(role);

		} catch (Exception e) {
			e.printStackTrace();
			return Results.failure("权限参数错误");
		}
		return Results.success();
	}

	@Override
	public Result<String> delete(String id) {
		Condition condition1 = new Condition(Inno72UserRole.class);
		condition1.createCriteria().andEqualTo("roleId", id);
		List<Inno72UserRole> userRoles = inno72UserRoleMapper.selectByCondition(condition1);
		if (userRoles != null && !userRoles.isEmpty()) {
			return Results.failure("删除失败，角色已被使用");
		}
		int row = inno72RoleMapper.deleteByPrimaryKey(id);
		if (row == 1) {
			Condition condition = new Condition(Inno72RoleFunction.class);
			condition.createCriteria().andEqualTo("roleId", id);
			inno72RoleFunctionMapper.deleteByCondition(condition);
			return Results.success();
		}
		return Results.failure("删除失败");
	}

}
