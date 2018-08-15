package com.inno72.system.mapper;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72UserDept;

@org.apache.ibatis.annotations.Mapper
public interface Inno72UserDeptMapper extends Mapper<Inno72UserDept> {

	int deleteByUserId(String userId);

	int deleteAll();
}
