package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72User;
import com.inno72.system.model.Inno72UserRole;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
public interface UserService extends Service<Inno72User> {

	Result<Inno72User> getUserByUserId(String userId);

	Result<List<Inno72User>> findUsers(String keyword);

	Result<String> auth(String userId, String roleIds);

	Result<List<Inno72UserRole>> queryUserRoles(String userId);

}
