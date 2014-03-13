package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Review implements Serializable {
	
	private static final long serialVersionUID = 6916560426949332001L;

	// Key is a system-generated encoded String
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String key;
	
	// The email of this Review's author
	@Persistent
	private String userEmail;
	
	// Between 0 and 10 (corresponds to 0-5 stars)
	@Persistent
	private int quality;
	
	// Between 1 and 5 (corresponds to 1-5 dollar signs)
	@Persistent
	private int cost;
	
	// Review text
	@Persistent
	private String text;

	// Default no-arg constructor required for serialization
	public Review() {
		this("", 0, 1, "");
	}
	
	public Review(String userEmail, int quality, int cost, String text)
			throws IllegalArgumentException {
		
		if (quality < 0 || quality > 10)
			throw new IllegalArgumentException("Invalid quality rating (must be between 0 and 10)");
		else
			this.quality = quality;
		
		if (cost < 1 || cost > 5)
			throw new IllegalArgumentException("Invalid cost rating (must be between 1 and 5)");
		else
			this.cost = cost;
		
		if (text != null && !text.isEmpty())
			this.text = text;
		else
			this.text = "";
		
		this.userEmail = userEmail;
	}
	
	public int getQuality() {
		return quality;
	}
	
	public int getCost() {
		return cost;
	}
	
	public String getText() {
		return text;
	}
	
	public String getUserEmail() {
		return userEmail;
	}

	@Override
	public String toString() {
		return "Review [userEmail=" + userEmail + ", quality=" + quality
				+ ", cost=" + cost + ", text=" + text + "]";
	}
}