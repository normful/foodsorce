package com.appspot.foodsorce.server;

import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class VendorJDO {

	// Key is an app-assigned string ID that corresponds
	// to the "key" column in new_food_vendor_locations.xls
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private String name;
	
	@Persistent
	private String description;
	
	@Persistent
	private String location;
	
	@Persistent
	private double latitude;
	
	@Persistent
	private double longitude;
	
	@Persistent
	private ArrayList<RatingJDO> ratings;
	
	public VendorJDO(String name, String description, String location,
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
		this.ratings = new ArrayList<RatingJDO>();
	}
	
	public void setKey(Key key) {
		this.key = key;
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
	
	public void addRating(RatingJDO rating) {
		ratings.add(rating);
	}
	
	public ArrayList<RatingJDO> getRatings() {
		return ratings;
	}
	
	public double getAverageCost() {
		double sum = 0.0;
		for (RatingJDO rating : ratings)
			sum += rating.getCost();
		return sum / ratings.size();
	}
	
	public double getAverageQuality() {
		double sum = 0.0;
		for (RatingJDO rating : ratings)
			sum += rating.getQuality();
		return sum / ratings.size();
	}

	@Override
	public String toString() {
		return "Vendor [key=" + key + ", name=" + name + ", description="
				+ description + ", location=" + location + ", latitude="
				+ latitude + ", longitude=" + longitude + ", ratings="
				+ ratings + "]";
	}
	
}