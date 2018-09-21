package com.inno72.service;

import com.inno72.common.Service;
import com.inno72.model.Inno72InteractMachineGoods;

import java.util.List;

/**
 * 机器商品service
 */
public interface Inno72InteractMachineGoodsService extends Service<Inno72InteractMachineGoods> {
    /**
     * 根据interactMachineId查询商品
     * @param interactMachineId
     * @return
     */
    List<Inno72InteractMachineGoods> findMachineGoods(String interactMachineId);
}
