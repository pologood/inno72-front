package com.inno72.service;

import java.util.List;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.vo.MachineVo;
import com.inno72.vo.QimenTmallFansAutomachineQureymachinesRequest;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72MachineService extends Service<Inno72Machine> {

	Result<Inno72MachineVo> findGame(String mid, String plantId, String version, String versionInno72);

	/**
	 * tmall.fans.automachine.savemachine( 注册、更新供应商上的设备信息到天猫互动吧 )
	 * @param machineId 监听到的ID
	 * @return MachineVo
	 */
	//查询机器下这个商品的个数
    Integer getMachineGoodsCount(String goodsId, String machineId);

    String findActivityIdByMachineCode(String machineCode);
}
