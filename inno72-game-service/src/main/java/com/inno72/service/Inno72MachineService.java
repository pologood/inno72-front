package com.inno72.service;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.Inno72MachineVo;
import com.inno72.common.Result;
import com.inno72.common.Service;


/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72MachineService extends Service<Inno72Machine> {

	Result<Inno72MachineVo> findGame(String mid, String gameId);
	
	Result<Object> createQrCode(String machineId);

	Result<Object> session_polling(String sessionUuid);
}
