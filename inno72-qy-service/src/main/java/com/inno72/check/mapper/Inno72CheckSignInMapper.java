package com.inno72.check.mapper;

import com.inno72.check.model.Inno72CheckSignIn;
import com.inno72.check.vo.MachineSignInVo;
import com.inno72.common.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@org.apache.ibatis.annotations.Mapper
public interface Inno72CheckSignInMapper extends Mapper<Inno72CheckSignIn> {
    List<Inno72CheckSignIn> selectTishMonth(@Param("checkUserId") String checkUserId, @Param("machineId") String machineId);

    List<MachineSignInVo> selectMachineSignList(String id);
}
