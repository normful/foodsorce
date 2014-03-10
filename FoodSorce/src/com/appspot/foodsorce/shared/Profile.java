package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Profile implements Serializable {

	private static final long serialVersionUID = 7197512350099924634L;

	// Key is an app-assigned string ID that is the user's email address
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String userEmail;
	
	@Persistent
	private String photoUrl;
	
	@Persistent
	private String gender;
	
	@Persistent
	private String headline;
	
	// Text of their favourite food. This is not a search setting.
	@Persistent
	private String favouriteFood;
	
	@Persistent
	private String hometown;
	
	@Persistent
	private String websiteUrl;
	
	public Profile(String userEmail) {
		this.userEmail = userEmail;
		this.photoUrl = "images/unknown_user.jpeg";
		this.gender = "";
		this.headline = "";
		this.favouriteFood = "";
		this.hometown = "";
		this.websiteUrl = "";
	}

	public String getUserEmail() {
		return userEmail;
	}

	public String getPhotoUrl() {
		return photoUrl;
	}

	public void setPhotoUrl(String photoUrl) {
		this.photoUrl = photoUrl;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getHeadline() {
		return headline;
	}

	public void setHeadline(String headline) {
		this.headline = headline;
	}

	public String getFavouriteFood() {
		return favouriteFood;
	}

	public void setFavouriteFood(String favouriteFood) {
		this.favouriteFood = favouriteFood;
	}

	public String getHometown() {
		return hometown;
	}

	public void setHometown(String hometown) {
		this.hometown = hometown;
	}

	public String getWebsiteUrl() {
		return websiteUrl;
	}

	public void setWebsiteUrl(String websiteUrl) {
		this.websiteUrl = websiteUrl;
	}

	@Override
	public String toString() {
		return "Profile [userEmail=" + userEmail + ", photoUrl="
				+ photoUrl + ", gender=" + gender + ", headline=" + headline
				+ ", favouriteFood=" + favouriteFood + ", hometown=" + hometown
				+ ", websiteUrl=" + websiteUrl + "]";
	}

}
