package com.inno72.vo;

import java.util.ArrayList;
import java.util.List;

import com.alibaba.fastjson.JSON;

public class QimenTmallFansAutomachineSynactoisvRequest {

	private FansActVO fansActVo;
	private Long ownerId;

	public static class FansActVO{

		private List<MachineActDetailDO> actDetailList;
		private MaterialDO materialDO;

		private String materialStatus;
		private Long actStatus;
		private String endTime;
		private String startTime;
		private Long actType;
		private Long shopId;
		private String sellerName;
		private Long sellerId;
		private String actName;
		private String actId;

		public List<MachineActDetailDO> getActDetailList() {
			return actDetailList;
		}

		public void setActDetailList(List<MachineActDetailDO> actDetailList) {
			this.actDetailList = actDetailList;
		}

		public MaterialDO getMaterialDO() {
			return materialDO;
		}

		public void setMaterialDO(MaterialDO materialDO) {
			this.materialDO = materialDO;
		}

		public String getMaterialStatus() {
			return materialStatus;
		}

		public void setMaterialStatus(String materialStatus) {
			this.materialStatus = materialStatus;
		}

		public Long getActStatus() {
			return actStatus;
		}

		public void setActStatus(Long actStatus) {
			this.actStatus = actStatus;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public Long getActType() {
			return actType;
		}

		public void setActType(Long actType) {
			this.actType = actType;
		}

		public Long getShopId() {
			return shopId;
		}

		public void setShopId(Long shopId) {
			this.shopId = shopId;
		}

		public String getSellerName() {
			return sellerName;
		}

		public void setSellerName(String sellerName) {
			this.sellerName = sellerName;
		}

		public Long getSellerId() {
			return sellerId;
		}

		public void setSellerId(Long sellerId) {
			this.sellerId = sellerId;
		}

		public String getActName() {
			return actName;
		}

		public void setActName(String actName) {
			this.actName = actName;
		}

		public String getActId() {
			return actId;
		}

		public void setActId(String actId) {
			this.actId = actId;
		}
	}

	static class MachineActDetailDO{

		private String actId;
		private String machineId;
		private String lockStatus;
		private String startTime;
		private String endTime;
		private String showAddress;

		public String getActId() {
			return actId;
		}

		public void setActId(String actId) {
			this.actId = actId;
		}

		public String getMachineId() {
			return machineId;
		}

		public void setMachineId(String machineId) {
			this.machineId = machineId;
		}

		public String getLockStatus() {
			return lockStatus;
		}

		public void setLockStatus(String lockStatus) {
			this.lockStatus = lockStatus;
		}

		public String getStartTime() {
			return startTime;
		}

		public void setStartTime(String startTime) {
			this.startTime = startTime;
		}

		public String getEndTime() {
			return endTime;
		}

		public void setEndTime(String endTime) {
			this.endTime = endTime;
		}

		public String getShowAddress() {
			return showAddress;
		}

		public void setShowAddress(String showAddress) {
			this.showAddress = showAddress;
		}
	}

	static class MaterialDO{

		private String actUrl;
		private String code;
		private String itemIds;

		public String getActUrl() {
			return actUrl;
		}

		public void setActUrl(String actUrl) {
			this.actUrl = actUrl;
		}

		public String getCode() {
			return code;
		}

		public void setCode(String code) {
			this.code = code;
		}

		public String getItemIds() {
			return itemIds;
		}

		public void setItemIds(String itemIds) {
			this.itemIds = itemIds;
		}
	}

	public FansActVO getFansActVo() {
		return fansActVo;
	}

	public void setFansActVo(FansActVO fansActVo) {
		this.fansActVo = fansActVo;
	}

	public Long getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(Long ownerId) {
		this.ownerId = ownerId;
	}

	public static void main(String [] args){
		QimenTmallFansAutomachineSynactoisvRequest req = new QimenTmallFansAutomachineSynactoisvRequest();
		FansActVO obj1 = new FansActVO();
		List<MachineActDetailDO> list3 = new ArrayList<MachineActDetailDO>();
		MachineActDetailDO obj4 = new MachineActDetailDO();
		list3.add(obj4);
		obj4.setActId("2839483");
		obj4.setMachineId("3948348");
		obj4.setLockStatus("1");
		obj4.setStartTime("2017-11-12");
		obj4.setEndTime("2017-11-12");
		obj4.setShowAddress("浙江省杭州市余杭区文一西路西西里");
		obj1.setActDetailList(list3);
		MaterialDO obj5 = new MaterialDO();
		obj5.setActUrl("https://www.tmall.com/");
		obj5.setCode("jdijisjs");
		obj5.setItemIds("32323,2323");
		obj1.setMaterialDO(obj5);
		obj1.setMaterialStatus("1");
		obj1.setActStatus(1L);
		obj1.setEndTime("2017-11-12");
		obj1.setStartTime("2017-11-22");
		obj1.setActType(1L);
		obj1.setShopId(34834L);
		obj1.setSellerName("测试商家");
		obj1.setSellerId(28928392L);
		obj1.setActName("耐克夏季促销");
		obj1.setActId("34343");
		req.setFansActVo(obj1);
		req.setOwnerId(33943948L);

		System.out.println(JSON.toJSONString(req));
	}
}
