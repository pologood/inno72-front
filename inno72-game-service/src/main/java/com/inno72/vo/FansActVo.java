package com.inno72.vo;

import java.util.List;

import lombok.Data;


/**
 * saveact( 保存更新活动信息至天猫 )
 * 活动信息类
 */
@Data
public class FansActVo {

	/**
	 * 活动ID
	 */
	private String actId;

	/**
	 * 活动名称  耐克夏季促销
	 */
	private String actName;
	/**
	 * 活动类型，1 天猫活动，2 非天猫活动
	 */
	private String actType;
	/**
	 * 活动对应的机器列表信息
	 */
	private List<MachineDetailVo> actDetailList;

	/**
	 * 活动结束时间
	 */
	private String endTime;

	/**
	 * 活动开始时间
	 */
	private String startTime;

	/**
	 * 活动对应的机器列表信息
	 */
	public class MachineDetailVo{
		/**
		 * 供应商ID
		 * ex: 232323
		 */
		private Integer ownerId;
		/**
		 * 活动开始时间
		 * ex: 2018-11-22
		 */
		private String startTime;
		/**
		 * 活动结束时间
		 *  ex: 2018-11-22
		 */
		private String endTime;
		/**
		 * 机器锁定状态
		 * ex: 1
		 */
		private Integer lockStatus;
		/**
		 * 机器ID
		 * ex: 23434233
		 */
		private String machineId;
		/**
		 * 活动ID
		 * ex: act_1234
		 */
		private String actId;
		/**
		 * 机器投放地址
		 * ex: 浙江省杭州市余杭区文一西路倾城里
		 */
		private String showAddress;
	}

}
