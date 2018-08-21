package com.inno72.vo;

import java.util.List;

/**
 * 奇门 分页请求我方机器信息
 */
public class QimenTmallFansAutomachineQureymachinesRequest {

	private String ownerId;

	private MachineQuery obj1;

	public String getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(String ownerId) {
		this.ownerId = ownerId;
	}

	public MachineQuery getObj1() {
		return obj1;
	}

	public void setObj1(MachineQuery obj1) {
		this.obj1 = obj1;
	}

	public class MachineQuery{
		private int pageSize;
		private int currentPage;
		private String machineId;
		private List<String> machineIdList;
		private String city;
		private String placeType;

		public int getPageSize() {
			return pageSize;
		}

		public void setPageSize(int pageSize) {
			this.pageSize = pageSize;
		}

		public int getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(int currentPage) {
			this.currentPage = currentPage;
		}

		public String getMachineId() {
			return machineId;
		}

		public void setMachineId(String machineId) {
			this.machineId = machineId;
		}

		public List<String> getMachineIdList() {
			return machineIdList;
		}

		public void setMachineIdList(List<String> machineIdList) {
			this.machineIdList = machineIdList;
		}

		public String getCity() {
			return city;
		}

		public void setCity(String city) {
			this.city = city;
		}

		public String getPlaceType() {
			return placeType;
		}

		public void setPlaceType(String placeType) {
			this.placeType = placeType;
		}
	}

}
