package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.Inno72MachineInformation;
import com.inno72.vo.Inno72TaoBaoCheckDataVo;

public interface PointService {
	/**
	 * 外部调用
	 *
	 * @param request requestBody
	 * @return 结果
	 */
	Result<String> information(String request);

	/**
	 * 内部调用保存新埋点数据
	 *
	 * @param session 必传
	 * @param enumInno72MachineInformationType 类型
	 * @return 结果
	 */
	Result<String> innerPoint(String session, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE enumInno72MachineInformationType);


	/**
	 * 淘宝数据同步数据保存本地备份接口，
	 * 注意vo中必填字段
	 * @param vo 请求对象
	 * @return 结果
	 */
	Result<String> innerTaoBaoDataSyn(Inno72TaoBaoCheckDataVo vo);
}
