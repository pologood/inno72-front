package com.inno72.mapper;

import com.inno72.common.Mapper;
import com.inno72.model.Inno72InteractMachine;
import com.inno72.vo.MachineSellerVo;
import org.apache.ibatis.annotations.Param;

import java.util.Date;
import java.util.List;

public interface Inno72InteractMachineMapper extends Mapper<Inno72InteractMachine> {
    List<Inno72InteractMachine> findActiveInteractMachine(@Param("machineCode") String machineCode);

    List<MachineSellerVo> findMachineIdAndSellerId(String[] interactId);
}