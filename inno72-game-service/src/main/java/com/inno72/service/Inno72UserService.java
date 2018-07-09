package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72User;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72UserService extends Service<Inno72User> {

	Inno72User getUser(String username);
}
