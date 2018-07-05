package com.inno72.service;
import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Authentication;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72AuthenticationService extends Service<Inno72Authentication> {

	Result<Object> login(String username,String password);
	
	Inno72Authentication getUser(String username);
}
