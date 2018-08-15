package com.inno72.system.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.system.model.Inno72Dept;

@org.apache.ibatis.annotations.Mapper
public interface Inno72DeptMapper extends Mapper<Inno72Dept> {

	int deleteAll();

	List<Inno72Dept> selectDeptsByPage(Map<String, Object> param);

}