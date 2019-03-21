package com.inno72.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import com.inno72.common.datetime.LocalDateTimeUtil;

public class RequestBody {

	private String requestId;

	private String ip;
	private String lat;
	private String lon;
	private String page;
	private int no;
	private String runTime;
	private String province;
	private String city;
	private String district;
	private String township;
	private String formatted_address;


	public RequestBody() {
	}

	/**
	 * @param ip
	 * @param lat
	 * @param lon
	 * @param page
	 */
	public RequestBody(int no, String requestId, String ip, String lat, String lon, String page) {

		this.requestId = requestId;
		this.ip = ip;
		this.lat = lat;
		this.lon = lon;
		this.page = page;
		this.no = no;
		this.runTime = LocalDateTimeUtil
				.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
	}

	public RequestBody(int no, String requestId, String ip, String lat, String lon, String page, String province,
			String city, String district, String township) {
		this.requestId = requestId;
		this.ip = ip;
		this.lat = lat;
		this.lon = lon;
		this.page = page;
		this.no = no;
		this.runTime = LocalDateTimeUtil
				.transfer(LocalDateTime.now(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
		this.province = province;
		this.city = city;
		this.district = district;
		this.township = township;
	}

	public int getNo() {
		return no;
	}

	public void setNo(int no) {
		this.no = no;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getLat() {
		return lat;
	}

	public void setLat(String lat) {
		this.lat = lat;
	}

	public String getLon() {
		return lon;
	}

	public void setLon(String lon) {
		this.lon = lon;
	}

	public String getPage() {
		return page;
	}

	public void setPage(String page) {
		this.page = page;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getRunTime() {
		return runTime;
	}

	public void setRunTime(String runTime) {
		this.runTime = runTime;
	}

	public String getProvince() {
		return province;
	}

	public void setProvince(String province) {
		this.province = province;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getTownship() {
		return township;
	}

	public void setTownship(String township) {
		this.township = township;
	}

	public String getFormatted_address() {
		return formatted_address;
	}

	public void setFormatted_address(String formatted_address) {
		this.formatted_address = formatted_address;
	}
}
