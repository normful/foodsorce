package com.appspot.foodsorce.client.vendor;

import java.io.Serializable;

public class VendorInfo implements Serializable {

	private static final long serialVersionUID = 5220128492323836009L;
	private int id;
	private String name;
	private String location;
	private String foodType;
	private double latitude;
	private double longitude;
	private double averageQuality;
	private double averageCost;

	public VendorInfo() {}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getFoodType() {
		return foodType;
	}

	public void setFoodType(String foodType) {
		this.foodType = foodType;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}

	public double getAverageQuality() {
		return averageQuality;
	}

	public void setAverageQuality(double averageQuality) {
		this.averageQuality = averageQuality;
	}

	public double getAverageCost() {
		return averageCost;
	}

	public void setAverageCost(double averageCost) {
		this.averageCost = averageCost;
	}

}
