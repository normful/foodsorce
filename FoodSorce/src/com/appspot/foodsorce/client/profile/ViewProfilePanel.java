package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewProfilePanel extends VerticalPanel {
	
	private String userEmail = "";
	private Profile user = new Profile();
	
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile</h2>");
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	
	public ViewProfilePanel(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty())
			this.userEmail = userEmail;
		
		getProfile();
		
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}
	
	private void getProfile() {
		profileService.getProfile(userEmail, new AsyncCallback<Profile>() {
			public void onFailure(Throwable error) {
				System.err.println("ViewProfilePanel.java: onFailure");
			}
			public void onSuccess(Profile result) {
				System.err.println("ViewProfilePanel.java: onFailure");
				
				Image profilePhoto = new Image(user.getPhotoUrl(), 0, 0, 225, 225);
				Label emailLabel = new Label("Email: " + userEmail);
				Label headlineLabel = new Label("Headline: " + user.getHeadline());
				Label genderLabel = new Label("Gender: " + user.getGender());
				Label favouriteFoodLabel = new Label("Favourite Food: " + user.getFavouriteFood());
				Label hometownLabel = new Label("Hometown: " + user.getHometown());
				Label websiteUrlLabel = new Label("Website: " + user.getWebsiteUrl());
				Anchor editProfileLink = new Anchor("Edit Profile");
//				editProfileLink.addClickHandler(new ClickHandler() {
//					public void onClick(ClickEvent event) {
//						loadEditProfile();
//					}
//				});
				
				htmlPanel.add(profilePhoto);
				htmlPanel.add(emailLabel);
				htmlPanel.add(headlineLabel);
				htmlPanel.add(genderLabel);
				htmlPanel.add(favouriteFoodLabel);
				htmlPanel.add(hometownLabel);
				htmlPanel.add(websiteUrlLabel);
				htmlPanel.add(editProfileLink);
			}
		});
	}
}
