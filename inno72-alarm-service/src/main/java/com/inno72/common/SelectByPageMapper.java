package com.inno72.common;

import java.util.List;

import org.apache.ibatis.annotations.SelectProvider;

import tk.mybatis.mapper.annotation.RegisterMapper;

@RegisterMapper
public interface SelectByPageMapper<T> {

	@SelectProvider(type = CustomSelectProvider.class, method = "dynamicSQL")
	List<T> selectByConditionByPage(Object condition);

}
