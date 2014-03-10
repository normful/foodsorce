package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rating implements Serializable {
	
	private static final long serialVersionUID = -3539849555657726056L;

	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	// Email of user who created this Rating
	@Persistent
	private String user;
	
	// Between 0 and 10 (corresponds to 0-5 stars)
	@Persistent
	private int quality;
	
	// Between 1 and 5 (corresponds to 1-5 dollar signs)
	@Persistent
	private int cost;
	
	@Persistent
	private String review;
	
	public Rating(int quality, int cost, String review, String user)
			throws IllegalArgumentException {
		
		if (quality < 0 || quality > 10)
			throw new IllegalArgumentException("Invalid quality (must be between 0 and 10)");
		else
			this.quality = quality;
		
		if (cost < 1 || cost > 5)
			throw new IllegalArgumentException("Invalid cost (must be between 1 and 5)");
		else
			this.cost = cost;
		
		this.review = review;
		this.user = user;
	}
	
	public int getQuality() {
		return quality;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getReview() {
		return review;
	}
	
	public String getUser() {
		return user;
	}
	
}