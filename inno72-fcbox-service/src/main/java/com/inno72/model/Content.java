package com.inno72.model;

public class Content {

	private String serviceType;
	private String address;
	private String serviceTime;
	private String longitude;
	private String latitude;

	public Content(String serviceType, String address, String serviceTime, String longitude, String latitude) {
		this.serviceType = serviceType;
		this.address = address;
		this.serviceTime = serviceTime;
		this.longitude = longitude;
		this.latitude = latitude;
	}

	public Content() {
	}

	public String getServiceType() {
		return serviceType;
	}

	public void setServiceType(String serviceType) {
		this.serviceType = serviceType;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getServiceTime() {
		return serviceTime;
	}

	public void setServiceTime(String serviceTime) {
		this.serviceTime = serviceTime;
	}

	public String getLongitude() {
		return longitude;
	}

	public void setLongitude(String longitude) {
		this.longitude = longitude;
	}

	public String getLatitude() {
		return latitude;
	}

	public void setLatitude(String latitude) {
		this.latitude = latitude;
	}
}
