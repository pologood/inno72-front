package com.inno72.service;


import com.inno72.common.Service;
import com.inno72.model.Inno72OrderRefund;

import java.util.List;

/**
 * Created by CodeGenerator on 2018/12/19.
 */
public interface Inno72OrderRefundService extends Service<Inno72OrderRefund> {
    /**
     * 根据orderId查找
     * @param orderId
     * @return
     */
    List<Inno72OrderRefund> findByOrderId(String orderId);
}
