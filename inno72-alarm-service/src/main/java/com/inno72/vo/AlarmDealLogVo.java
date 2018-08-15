package com.inno72.vo;

import java.util.List;

import com.inno72.model.AlarmDealLog;
import com.inno72.model.AlarmDetailLog;

public class AlarmDealLogVo {

	private AlarmDealLog alarmDealLog;

	private List<AlarmDetailLog> logs;

	public AlarmDealLog getAlarmDealLog() {
		return alarmDealLog;
	}

	public void setAlarmDealLog(AlarmDealLog alarmDealLog) {
		this.alarmDealLog = alarmDealLog;
	}

	public List<AlarmDetailLog> getLogs() {
		return logs;
	}

	public void setLogs(List<AlarmDetailLog> logs) {
		this.logs = logs;
	}
}
