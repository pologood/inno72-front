package com.inno72.mapper;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MerchantTotalCountByUser;

public interface Inno72MerchantTotalCountByUserMapper extends Mapper<Inno72MerchantTotalCountByUser> {
	List<Inno72MerchantTotalCountByUser> selectByActivityIdByPage(@Param("activityId") String activityId, @Param("start") String start,
			@Param("end") String end, @Param("page") int page, @Param("pageSize") int pageSize);
	List<Inno72MerchantTotalCountByUser> selectByActivityId(@Param("activityId") String activityId, @Param("start") String start,
			@Param("end") String end);
	Long selectByActivityIdCount(@Param("activityId") String activityId,@Param("start") String start,@Param("end") String end);
	String selectUserMobile(String userId);
}