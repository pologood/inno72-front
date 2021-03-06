package com.inno72.service;

import java.util.List;
import java.util.Map;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72MerchantTotalCount;
import com.inno72.vo.ActMerchantLog;
import com.inno72.vo.Inno72MerchantTotalCountVo;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
public interface Inno72MerchantTotalCountService extends Service<Inno72MerchantTotalCount> {

	Result<List<Inno72MerchantTotalCountVo>> findAllById(String id);

	Result<Object> totle(String id);

	Result<List<Map<String,Object>>> addressNum(String actId);

	Result<List<ActMerchantLog>> actLog(String actId, String merchantId);

	Result<Inno72MerchantTotalCountVo> actInfo(String actId, String merchantId);
}
