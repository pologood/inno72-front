package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MerchantTotalCountByOrderMapper;
import com.inno72.model.Inno72MerchantTotalCountByOrder;
import com.inno72.service.Inno72MerchantTotalCountByOrderService;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountByOrderServiceImpl extends AbstractService<Inno72MerchantTotalCountByOrder> implements Inno72MerchantTotalCountByOrderService {
    @Resource
    private Inno72MerchantTotalCountByOrderMapper inno72MerchantTotalCountByOrderMapper;

}
