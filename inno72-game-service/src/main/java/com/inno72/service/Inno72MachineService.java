package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.common.Service;
import com.inno72.model.Inno72Machine;
import com.inno72.vo.Inno72MachineVo;

/**
 * Created by CodeGenerator on 2018/06/27.
 */
public interface Inno72MachineService extends Service<Inno72Machine> {

	Result<Inno72MachineVo> findGame(String mid, String gameId, String version, String versionInno72);

}
