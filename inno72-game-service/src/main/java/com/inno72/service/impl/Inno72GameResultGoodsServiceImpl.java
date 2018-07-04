package com.inno72.service.impl;

import com.inno72.mapper.Inno72GameResultGoodsMapper;
import com.inno72.model.Inno72GameResultGoods;
import com.inno72.service.Inno72GameResultGoodsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2018/07/02.
 */
@Service
@Transactional
public class Inno72GameResultGoodsServiceImpl extends AbstractService<Inno72GameResultGoods> implements Inno72GameResultGoodsService {
    @Resource
    private Inno72GameResultGoodsMapper inno72GameResultGoodsMapper;

}
