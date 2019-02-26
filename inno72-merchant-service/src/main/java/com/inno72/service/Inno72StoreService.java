package com.inno72.service;
import java.util.List;

import com.inno72.common.Result;
import com.inno72.model.Inno72Store;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
public interface Inno72StoreService extends Service<Inno72Store> {

	Result<List<Inno72Store>> selectAll();

}
