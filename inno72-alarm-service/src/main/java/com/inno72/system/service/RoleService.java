package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72Role;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
public interface RoleService extends Service<Inno72Role> {

	Result<List<Inno72Role>> findRoles(String keyword);

	Result<String> add(String name, String auths);

	Result<String> update(String id, String name, String auths);

	Result<String> delete(String id);

}
