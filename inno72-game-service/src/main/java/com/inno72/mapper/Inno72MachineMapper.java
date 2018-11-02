package com.inno72.mapper;

import java.util.List;
import java.util.Map;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72AdminArea;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.MachineVo;

public interface Inno72MachineMapper extends Mapper<Inno72Machine> {

	Inno72Machine findMachineByCode(String machineCode);

	List<MachineVo> queryQimenMachineListByPage(Map<String, Object> params);

    Inno72AdminArea findAreaByMachineCode(String machineCode);
}