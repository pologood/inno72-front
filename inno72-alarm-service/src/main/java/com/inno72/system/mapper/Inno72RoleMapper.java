package com.inno72.system.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72Role;

@org.apache.ibatis.annotations.Mapper
public interface Inno72RoleMapper extends Mapper<Inno72Role> {

	List<Inno72Role> selectRolesByPage(Map<String, Object> param);
}