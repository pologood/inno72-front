package com.inno72.service;

import java.io.IOException;

import com.inno72.model.Inno72MachineConnectionMsg;

public interface ConnectionMsgService {
	void execute(Integer type);
	void sendMsg(Inno72MachineConnectionMsg msg) throws IOException;
}
