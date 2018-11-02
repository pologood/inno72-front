package com.inno72.service;

import com.inno72.common.Result;
import com.inno72.vo.Inno72MachineInformation;

public interface PointService {
	Result<String> information(String request);
	Result<String> innerPoint(String session, Inno72MachineInformation.ENUM_INNO72_MACHINE_INFORMATION_TYPE enumInno72MachineInformationType);
}
