package com.inno72.vo;

import java.util.List;


/**
 * saveact( 保存更新活动信息至天猫 )
 * 活动信息类
 */
public class FansActVo {

	/**
	 * 活动ID
	 */
	private String act_id;

	/**
	 * 活动名称  耐克夏季促销
	 */
	private String act_name;
	/**
	 * 活动类型，1 天猫活动，2 非天猫活动
	 */
	private String act_type;
	/**
	 * 活动对应的机器列表信息
	 */
	private List<MachineDetailVo> act_detail_list;

	/**
	 * 活动结束时间
	 */
	private String end_time;

	/**
	 * 活动开始时间
	 */
	private String start_time;

	/**
	 * 活动对应的机器列表信息
	 */
	public class MachineDetailVo{
		/**
		 * 供应商ID
		 * ex: 232323
		 */
		private Number owner_id;
		/**
		 * 活动开始时间
		 * ex: 2018-11-22
		 */
		private String start_time;
		/**
		 * 活动结束时间
		 *  ex: 2018-11-22
		 */
		private String end_time;
		/**
		 * 机器锁定状态
		 * ex: 1
		 */
		private Number lock_status;
		/**
		 * 机器ID
		 * ex: 23434233
		 */
		private String machine_id;
		/**
		 * 活动ID
		 * ex: act_1234
		 */
		private String act_id;
		/**
		 * 机器投放地址
		 * ex: 浙江省杭州市余杭区文一西路倾城里
		 */
		private String show_address;

		public Number getOwner_id() {
			return owner_id;
		}

		public void setOwner_id(Number owner_id) {
			this.owner_id = owner_id;
		}

		public String getStart_time() {
			return start_time;
		}

		public void setStart_time(String start_time) {
			this.start_time = start_time;
		}

		public String getEnd_time() {
			return end_time;
		}

		public void setEnd_time(String end_time) {
			this.end_time = end_time;
		}

		public Number getLock_status() {
			return lock_status;
		}

		public void setLock_status(Number lock_status) {
			this.lock_status = lock_status;
		}

		public String getMachine_id() {
			return machine_id;
		}

		public void setMachine_id(String machine_id) {
			this.machine_id = machine_id;
		}

		public String getAct_id() {
			return act_id;
		}

		public void setAct_id(String act_id) {
			this.act_id = act_id;
		}

		public String getShow_address() {
			return show_address;
		}

		public void setShow_address(String show_address) {
			this.show_address = show_address;
		}
	}

	public String getAct_id() {
		return act_id;
	}

	public void setAct_id(String act_id) {
		this.act_id = act_id;
	}

	public String getAct_name() {
		return act_name;
	}

	public void setAct_name(String act_name) {
		this.act_name = act_name;
	}

	public String getAct_type() {
		return act_type;
	}

	public void setAct_type(String act_type) {
		this.act_type = act_type;
	}

	public List<MachineDetailVo> getAct_detail_list() {
		return act_detail_list;
	}

	public void setAct_detail_list(List<MachineDetailVo> act_detail_list) {
		this.act_detail_list = act_detail_list;
	}

	public String getEnd_time() {
		return end_time;
	}

	public void setEnd_time(String end_time) {
		this.end_time = end_time;
	}

	public String getStart_time() {
		return start_time;
	}

	public void setStart_time(String start_time) {
		this.start_time = start_time;
	}
}
