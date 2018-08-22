package com.inno72.machine.mapper;

import com.inno72.common.Mapper;
import com.inno72.machine.model.Inno72Machine;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@org.apache.ibatis.annotations.Mapper
public interface Inno72MachineMapper extends Mapper<Inno72Machine> {


	List<Inno72Machine> getMachine(String checkUserId);

	List<Inno72Machine> getMachineByLackGoods(@Param("checkUserId") String checkUserId, @Param("goodsId") String goodsId);

    List<Inno72Machine> machineList(String chekUserId);

    List<Inno72Machine> selectByParam(Map<String, Object> map);

    Inno72Machine getMachineByCode(String machineCode);

}