package com.inno72.model;


import javax.persistence.Id;

import lombok.Data;

@Data
public class Points {
	@Id
	private String id;
	private Double lon;
	private Double lat;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}
}
