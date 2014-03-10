package com.appspot.foodsorce.client;

import java.io.Serializable;
import java.util.ArrayList;

public class Vendor implements Serializable {
	
	private static final long serialVersionUID = -3415324477148115200L;

	private String name;
	private String description;
	private String location;
	private double latitude;
	private double longitude;
	private ArrayList<Rating> ratings;
	
	public Vendor(String name, String description, String location,
			double latitude, double longitude) {
		
		if (name != null && name.isEmpty())
			this.name = description;
		else
			this.name = name;
		
		if (description != null && !description.isEmpty())
			this.description = description;
		
		if (location != null && !location.isEmpty())
			this.location = location;
		
		this.latitude = latitude;
		this.longitude = longitude;
		this.ratings = new ArrayList<Rating>();
	}
	
	public String getName() {
		return name;
	}
	
	public String getDescription() {
		return description;
	}
	public String getLocation() {
		return location;
	}
	
	public double getLatitude() {
		return latitude;
	}
	
	public double getLongitude() {
		return longitude;
	}
	
	public void addRating(Rating rating) {
		ratings.add(rating);
	}
	
	public ArrayList<Rating> getRatings() {
		return ratings;
	}
	
	public double getAverageCost() {
		double sum = 0.0;
		for (Rating rating : ratings)
			sum += rating.getCost();
		return sum / ratings.size();
	}
	
	public double getAverageQuality() {
		double sum = 0.0;
		for (Rating rating : ratings)
			sum += rating.getQuality();
		return sum / ratings.size();
	}

	@Override
	public String toString() {
		return "Vendor [name=" + name + ", description="
				+ description + ", location=" + location + ", latitude="
				+ latitude + ", longitude=" + longitude + ", ratings="
				+ ratings + "]";
	}
	
}