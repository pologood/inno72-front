package com.inno72.service.impl;

import com.inno72.common.AbstractService;
import com.inno72.mapper.Inno72InteractMachineGoodsMapper;
import com.inno72.model.Inno72InteractMachineGoods;
import com.inno72.service.Inno72InteractMachineGoodsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class Inno72InteractMachineGoodsServiceImpl  extends AbstractService<Inno72InteractMachineGoods> implements Inno72InteractMachineGoodsService {

    @Autowired
    private Inno72InteractMachineGoodsMapper mapper;
    @Override
    public List<Inno72InteractMachineGoods> findMachineGoods(String interactMachineId) {
        List<Inno72InteractMachineGoods> list = mapper.findMachineGoods(interactMachineId);
        return list;
    }
}
