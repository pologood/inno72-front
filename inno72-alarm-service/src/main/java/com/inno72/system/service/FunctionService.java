package com.inno72.system.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.system.model.Inno72Function;
import com.inno72.system.vo.FunctionTreeResultVo;

/**
 * Created by CodeGenerator on 2018/07/05.
 */
public interface FunctionService extends Service<Inno72Function> {

	List<Inno72Function> findFunctionsByUserId(String id);

	Result<List<Inno72Function>> findFunctions(String keyword);

	Result<FunctionTreeResultVo> findAllTree(String roleId);

}
