package com.appspot.foodsorce.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Vendor implements Serializable {

	private static final long serialVersionUID = -6615277218094118492L;

	// Key is an app-assigned string ID that corresponds
	// to the "key" column in new_food_vendor_locations.xls
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String encodedKey;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String excelKey;
	
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
	
	public Vendor(String excelKey, String name, String description, String location,
			double latitude, double longitude) {
		
		if (excelKey != null && !excelKey.isEmpty())
			this.excelKey = excelKey;
		
		if (name != null && !name.isEmpty())
			this.name = name;
		else
			this.name = description;
		
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
		return "Vendor [excelKey=" + excelKey + ", name=" + name + ", description="
				+ description + ", location=" + location + ", latitude="
				+ latitude + ", longitude=" + longitude + ", ratings="
				+ ratings + "]";
	}
	
}