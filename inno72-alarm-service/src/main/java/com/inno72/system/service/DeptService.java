package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72Dept;

/**
 * Created by CodeGenerator on 2018/07/03.
 */
public interface DeptService extends Service<Inno72Dept> {

	Result deleteAll();

	Result<List<Inno72Dept>> findDepts(String keyword);

}
