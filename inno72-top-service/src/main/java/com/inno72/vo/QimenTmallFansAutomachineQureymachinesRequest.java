package com.inno72.vo;

import java.util.List;

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

	class MachineQuery{
		private Long pageSize;
		private Long currentPage;
		private String machineId;
		private List<String> machineIdList;
		private String city;
		private String placeType;

		public Long getPageSize() {
			return pageSize;
		}

		public void setPageSize(Long pageSize) {
			this.pageSize = pageSize;
		}

		public Long getCurrentPage() {
			return currentPage;
		}

		public void setCurrentPage(Long currentPage) {
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
