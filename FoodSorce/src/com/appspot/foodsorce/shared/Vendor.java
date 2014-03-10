package com.appspot.foodsorce.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Vendor implements Serializable {

	private static final long serialVersionUID = 1919782205348732198L;

	// Key is an app-assigned string ID
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
	
}