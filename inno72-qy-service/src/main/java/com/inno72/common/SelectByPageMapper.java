package com.inno72.common;

import org.apache.ibatis.annotations.SelectProvider;
import tk.mybatis.mapper.annotation.RegisterMapper;

import java.util.List;

@RegisterMapper
public interface SelectByPageMapper<T> {

	@SelectProvider(type = CustomSelectProvider.class, method = "dynamicSQL")
	List<T> selectByConditionByPage(Object condition);

}
