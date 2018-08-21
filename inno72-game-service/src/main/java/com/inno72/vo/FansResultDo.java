package com.inno72.vo;

import java.util.List;

import lombok.Data;

@Data
public class FansResultDo {

	private boolean success;
	private String msg_code;
	private String msg_info;

	private List<MachineVo> model;


}
