package com.inno72.service.impl;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72MerchantTotalCountByGoodsMapper;
import com.inno72.model.Inno72MerchantTotalCountByGoods;
import com.inno72.service.Inno72MerchantTotalCountByGoodsService;


/**
 * Created by CodeGenerator on 2018/11/07.
 */
@Service
@Transactional
public class Inno72MerchantTotalCountByGoodsServiceImpl extends AbstractService<Inno72MerchantTotalCountByGoods> implements Inno72MerchantTotalCountByGoodsService {
    @Resource
    private Inno72MerchantTotalCountByGoodsMapper inno72MerchantTotalCountByGoodsMapper;

}
