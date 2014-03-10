package com.appspot.foodsorce.server;

import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.users.User;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class ProfileJDO {

	// Key is a system-generated numeric ID
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	private Key key;
	
	@Persistent
	private User user;
	
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
	
	public ProfileJDO(User user) {
		this.user = user;
		this.photoUrl = "images/unknown_user.jpeg";
		this.gender = "";
		this.headline = "";
		this.favouriteFood = "";
		this.hometown = "";
		this.websiteUrl = "";
	}

	public User getUser() {
		return this.user;
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
		return "Profile [key=" + key + ", user=" + user + ", photoUrl="
				+ photoUrl + ", gender=" + gender + ", headline=" + headline
				+ ", favouriteFood=" + favouriteFood + ", hometown=" + hometown
				+ ", websiteUrl=" + websiteUrl + "]";
	}

}
