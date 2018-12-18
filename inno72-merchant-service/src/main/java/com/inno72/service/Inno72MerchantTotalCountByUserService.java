package com.inno72.service;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.model.Inno72MerchantTotalCountByUser;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/12/08.
 */
public interface Inno72MerchantTotalCountByUserService extends Service<Inno72MerchantTotalCountByUser> {

	public Result<Map<String, Object>> selectByActivityId(String activityId, String start, String end);
}
