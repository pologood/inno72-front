package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72MerchantUser;
import com.inno72.vo.UserSessionVo;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
public interface Inno72MerchantUserService extends Service<Inno72MerchantUser> {

	Result<UserSessionVo> login(String userName, String password);

	Result resetPwd(String id, String password, String confirm, String oPassword);

	Result resetPhone(String id, String phone);
}
