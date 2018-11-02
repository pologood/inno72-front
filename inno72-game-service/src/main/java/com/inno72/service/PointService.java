package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.Inno72TaoBaoCheckDataVo;

public interface PointService {
	Result<String> information(String request);
	Result<String> innerPoint(String session, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE enumInno72MachineInformationType);


	/**
	 * 淘宝数据同步数据保存本地备份接口，
	 * 注意vo中必填字段
	 * @param vo 请求对象
	 * @return 结果
	 */
	Result<String> innerTaoBaoDataSyn(Inno72TaoBaoCheckDataVo vo);
}
