package com.inno72.system.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72UserDept;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
public interface UserDeptService extends Service<Inno72UserDept> {

	Result<String> deleteAll();

	Result<String> deleteByUserId(String userId);

}
