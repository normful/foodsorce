package com.appspot.foodsorce.shared;

import java.io.Serializable;
import java.util.ArrayList;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class Vendor implements Serializable {

	private static final long serialVersionUID = 6798024041102667120L;

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
	
	@Persistent(serialized="true", defaultFetchGroup="true")
	private ArrayList<Review> reviews;
	
	// Default no-arg constructor required for serialization
	public Vendor() {
		this("", "", "", "", 0.0, 0.0);
	}
	
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
		reviews = new ArrayList<Review>();
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
	
	public void addReview(Review review) {
		reviews.add(review);
	}
	
	public ArrayList<Review> getReviews() {
		return reviews;
	}
	
	public double getAverageCost() {
		if (reviews.isEmpty())
			return -1;
		double sum = 0.0;
		for (Review review : reviews)
			sum += review.getCost();
		return sum / reviews.size();
	}
	
	public double getAverageQuality() {
		if (reviews.isEmpty())
			return -1;
		double sum = 0.0;
		for (Review review : reviews)
			sum += (review.getQuality() / 2);
		return sum / reviews.size();
	}

	@Override
	public String toString() {
		return "Vendor [excelKey=" + excelKey + ", name=" + name + ", description="
				+ description + ", location=" + location + ", latitude="
				+ latitude + ", longitude=" + longitude + ", reviews="
				+ reviews + "]";
	}
	
}