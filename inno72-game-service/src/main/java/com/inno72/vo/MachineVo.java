package com.inno72.vo;

import java.util.List;

/**
 * 请求淘宝，注册、更新供应商上的设备信息到天猫互动吧 savemachine
 */
public class MachineVo {
	/**
	 * 机器ID
	 */
	private String machine_id;
	/**
	 * 机器名称
	 */
	private String machine_name;
	/**
	 * 机器状态 0未启用，1启用 -1废弃
	 */
	private Number machine_status;
	/**
	 * 设备图片链接集合 必须是淘系可访问的图片地址
	 */
	private List<String> machine_pics;
	/**
	 * 设备描述
	 */
	private String machine_desc;
	/**
	 * 机器点位信息 ==> AddressVo
	 */
	private AddessVo address_v_o;
	/**
	 * 供应商的唯一ID，淘宝ID
	 */
	private Number owner_id;
	/**
	 * 供应商平成 XX公司
	 */
	private String owner_name;
	/**
	 * office：办公写字楼，
	 * shopping_mall:商场， subway:地铁,
	 * airport:机场， cuntryside:村淘,
	 * other:其他 irregular:机动位置，
	 * 位置不固定;
	 */
	private String place_type;
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
	public class AddessVo{
		/**
		 * 详细地址 969号小厕所
		 */
		private String detail_address;
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
		private String show_city;
		/**
		 * 浙江省
		 */
		private String province;

		public String getDetail_address() {
			return detail_address;
		}

		public void setDetail_address(String detail_address) {
			this.detail_address = detail_address;
		}

		public String getStreet() {
			return street;
		}

		public void setStreet(String street) {
			this.street = street;
		}

		public String getArea() {
			return area;
		}

		public void setArea(String area) {
			this.area = area;
		}

		public String getShow_city() {
			return show_city;
		}

		public void setShow_city(String show_city) {
			this.show_city = show_city;
		}

		public String getProvince() {
			return province;
		}

		public void setProvince(String province) {
			this.province = province;
		}
	}

	public String getMachine_id() {
		return machine_id;
	}

	public void setMachine_id(String machine_id) {
		this.machine_id = machine_id;
	}

	public String getMachine_name() {
		return machine_name;
	}

	public void setMachine_name(String machine_name) {
		this.machine_name = machine_name;
	}

	public Number getMachine_status() {
		return machine_status;
	}

	public void setMachine_status(Number machine_status) {
		this.machine_status = machine_status;
	}

	public List<String> getMachine_pics() {
		return machine_pics;
	}

	public void setMachine_pics(List<String> machine_pics) {
		this.machine_pics = machine_pics;
	}

	public String getMachine_desc() {
		return machine_desc;
	}

	public void setMachine_desc(String machine_desc) {
		this.machine_desc = machine_desc;
	}

	public AddessVo getAddress_v_o() {
		return address_v_o;
	}

	public void setAddress_v_o(AddessVo address_v_o) {
		this.address_v_o = address_v_o;
	}

	public Number getOwner_id() {
		return owner_id;
	}

	public void setOwner_id(Number owner_id) {
		this.owner_id = owner_id;
	}

	public String getOwner_name() {
		return owner_name;
	}

	public void setOwner_name(String owner_name) {
		this.owner_name = owner_name;
	}

	public String getPlace_type() {
		return place_type;
	}

	public void setPlace_type(String place_type) {
		this.place_type = place_type;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}
}
