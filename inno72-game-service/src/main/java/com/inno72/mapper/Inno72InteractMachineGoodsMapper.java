package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72InteractMachineGoods;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface Inno72InteractMachineGoodsMapper extends Mapper<Inno72InteractMachineGoods> {
    List<Inno72InteractMachineGoods> findMachineGoods(@Param("interactMachineId") String interactMachineId);
}