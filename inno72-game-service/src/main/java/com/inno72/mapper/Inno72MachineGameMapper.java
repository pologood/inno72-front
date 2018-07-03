package com.inno72.mapper;

import java.util.List;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72MachineGame;

public interface Inno72MachineGameMapper extends Mapper<Inno72MachineGame> {

	List<Inno72MachineGame> selectByMachineId(String machineId);
}