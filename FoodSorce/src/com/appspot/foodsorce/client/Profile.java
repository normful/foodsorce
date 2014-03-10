package com.appspot.foodsorce.client;

import java.io.Serializable;

import com.google.appengine.api.users.User;

public class Profile implements Serializable {

	private static final long serialVersionUID = -9026384283046797137L;
	
	private User user;
	private String photoUrl;
	private String gender;
	private String headline;
	// Text of their favourite food. This is not a search setting.
	private String favouriteFood;
	private String hometown;
	private String websiteUrl;
	
	public Profile(User user) {
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
		return "Profile [user=" + user + ", photoUrl="
				+ photoUrl + ", gender=" + gender + ", headline=" + headline
				+ ", favouriteFood=" + favouriteFood + ", hometown=" + hometown
				+ ", websiteUrl=" + websiteUrl + "]";
	}

}
