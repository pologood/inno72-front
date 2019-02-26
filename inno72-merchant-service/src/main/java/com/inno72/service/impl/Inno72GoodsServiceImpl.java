package com.inno72.service.impl;

import com.inno72.mapper.Inno72GoodsMapper;
import com.inno72.model.Inno72Goods;
import com.inno72.service.Inno72GoodsService;
import com.inno72.common.AbstractService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;


/**
 * Created by CodeGenerator on 2019/01/02.
 */
@Service
@Transactional
public class Inno72GoodsServiceImpl extends AbstractService<Inno72Goods> implements Inno72GoodsService {
    @Resource
    private Inno72GoodsMapper inno72GoodsMapper;

}
