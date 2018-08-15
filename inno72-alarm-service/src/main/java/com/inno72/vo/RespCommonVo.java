package com.inno72.vo;

import java.util.List;

import com.inno72.model.AlarmMsgType;
import com.inno72.model.AlarmUser;

import lombok.Data;

@Data
public class RespCommonVo {

	private List<AlarmUser> alarmUser;

	private List<AlarmMsgType> alarmMsgTypes;
}
