package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72MerchantTotalCount;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
public interface Inno72MerchantTotalCountService extends Service<Inno72MerchantTotalCount> {

	Result<List<Inno72MerchantTotalCount>> findAllById(String id);

	Result<Object> totle(String id);
}
