package com.inno72.vo;

import java.util.List;

import lombok.Data;

/**
 * 请求淘宝，注册、更新供应商上的设备信息到天猫互动吧 savemachine
 */
@Data
public class MachineVo {
	/**
	 * 机器ID
	 */
	private String machineId;
	/**
	 * 机器名称
	 */
	private String machineName;
	/**
	 * 机器状态 0未启用，1启用 -1废弃
	 */
	private Integer machineStatus;
	/**
	 * 设备图片链接集合 必须是淘系可访问的图片地址
	 */
	private List<String> machinePics;
	/**
	 * 设备描述
	 */
	private String machineDesc;
	/**
	 * 机器点位信息 ==> AddressVo
	 */
	private AddessVo addressVo;
	/**
	 * 供应商的唯一ID，淘宝ID
	 */
	private Integer ownerId;
	/**
	 * 供应商平成 XX公司
	 */
	private String ownerName;
	/**
	 * office：办公写字楼，
	 * shopping_mall:商场， subway:地铁,
	 * airport:机场， cuntryside:村淘,
	 * other:其他 irregular:机动位置，
	 * 位置不固定;
	 */
	private String placeType;
	/**
	 * 点位经度
	 */
	private String latitude;
	/**
	 * 点位纬度
	 */
	private String longitude;

	/**
	 * 机器点位地址，遵守菜鸟的5及地址规则，xx省XX市XX区XX街道XX详细地址
	 */
	@Data
	public class AddessVo{
		/**
		 * 详细地址 969号小厕所
		 */
		private String detailAddress;
		/**
		 * 街道 文一路
		 */
		private String street;
		/**
		 * 区 余杭区
		 */
		private String area;
		/**
		 * 杭州市
		 */
		private String showCity;
		/**
		 * 浙江省
		 */
		private String province;

	}

}
