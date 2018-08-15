package com.inno72.task;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.alibaba.fastjson.JSON;
import com.inno72.common.Result;
import com.inno72.service.AlarmUserService;

public class TaskService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TaskService.class);

	@Autowired
	private AlarmUserService alarmUserService;


	public void syncUser(){
		LOGGER.info("**************************** 开始同步用户任务 **************************** ");
		Result<String> stringResult = alarmUserService.syncUser();
		LOGGER.info("同步结果 {}", JSON.toJSONString(stringResult));
	}

}
