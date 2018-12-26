package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72OrderRefundMapper;
import com.inno72.model.Inno72OrderRefund;
import com.inno72.service.Inno72OrderRefundService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;


/**
 * Created by CodeGenerator on 2018/12/19.
 */
@Service
@Transactional
public class Inno72OrderRefundServiceImpl extends AbstractService<Inno72OrderRefund> implements
		Inno72OrderRefundService {
    @Resource
    private Inno72OrderRefundMapper inno72OrderRefundMapper;

    @Override
    public List<Inno72OrderRefund> findByOrderId(String orderId) {
        Inno72OrderRefund param = new Inno72OrderRefund();
        param.setOrderId(orderId);
        return inno72OrderRefundMapper.select(param);
    }
}
