package com.inno72.service;
import com.inno72.common.Result;
import com.inno72.model.Inno72StoreOrder;
import com.inno72.common.Service;
import com.inno72.vo.Inno72StoreVo;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
public interface Inno72StoreOrderService extends Service<Inno72StoreOrder> {

	Result<String> addOrder(Inno72StoreVo vo);
}
