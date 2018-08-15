package com.inno72.system.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.common.Result;
import com.inno72.common.ResultGenerator;
import com.inno72.system.mapper.Inno72UserDeptMapper;
import com.inno72.system.model.Inno72UserDept;
import com.inno72.system.service.UserDeptService;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
@Service
@Transactional
public class UserDeptServiceImpl extends AbstractService<Inno72UserDept> implements UserDeptService {
	@Resource
	private Inno72UserDeptMapper inno72UserDeptMapper;

	@Override
	public Result<String> deleteAll() {
		inno72UserDeptMapper.deleteAll();
		return ResultGenerator.genSuccessResult();
	}

	@Override
	public Result<String> deleteByUserId(String userId) {
		inno72UserDeptMapper.deleteByUserId(userId);
		return ResultGenerator.genSuccessResult();
	}

}
