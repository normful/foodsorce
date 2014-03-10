package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Rating implements Serializable {
	
	private static final long serialVersionUID = -3539849555657726056L;

	// Key is a system-generated numeric ID
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	// Author of this Rating
	@Persistent
	private User user;
	
	// Between 0 and 10 (corresponds to 0-5 stars)
	@Persistent
	private int quality;
	
	// Between 1 and 5 (corresponds to 1-5 dollar signs)
	@Persistent
	private int cost;
	
	// Review text
	@Persistent
	private String review;
	
	public Rating(int quality, int cost, String review, User user)
			throws IllegalArgumentException {
		
		if (quality < 0 || quality > 10)
			throw new IllegalArgumentException("Invalid quality (must be between 0 and 10)");
		else
			this.quality = quality;
		
		if (cost < 1 || cost > 5)
			throw new IllegalArgumentException("Invalid cost (must be between 1 and 5)");
		else
			this.cost = cost;
		
		if (review != null && !review.isEmpty())
			this.review = review;
		else
			this.review = "";
		
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
	
	public User getUser() {
		return user;
	}

	@Override
	public String toString() {
		return "Rating [key=" + key + ", user=" + user + ", quality=" + quality
				+ ", cost=" + cost + ", review=" + review + "]";
	}
	
}