package com.inno72.model;

public class PointPlan {
	private int id;
	private Double lat;
	private Double lon;

	public PointPlan() {
	}

	/**
	 * @param id
	 * @param lat
	 * @param lon
	 */
	public PointPlan(int id, Double lat, Double lon) {
		this.id = id;
		this.lat = lat;
		this.lon = lon;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Double getLat() {
		return lat;
	}

	public void setLat(Double lat) {
		this.lat = lat;
	}

	public Double getLon() {
		return lon;
	}

	public void setLon(Double lon) {
		this.lon = lon;
	}
}
