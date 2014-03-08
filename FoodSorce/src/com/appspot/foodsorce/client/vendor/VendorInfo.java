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

	public VendorInfo(String name, String location, String foodType,
			double latitude, double longitude) {
		this.name = name;
		this.location = location;
		this.foodType = foodType;
		this.latitude = latitude;
		this.longitude = longitude;
		this.averageCost = 0.0;
		this.averageQuality = 0.0;
	}

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

	@Override
	public String toString() {
		return "VendorInfo [id=" + id + ", name=" + name + ", location="
				+ location + ", foodType=" + foodType + ", latitude="
				+ latitude + ", longitude=" + longitude + ", averageQuality="
				+ averageQuality + ", averageCost=" + averageCost + "]";
	}

}
