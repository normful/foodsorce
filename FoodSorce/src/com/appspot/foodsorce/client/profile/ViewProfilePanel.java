package com.appspot.foodsorce.client.profile;

import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewProfilePanel extends VerticalPanel {
	
	public ViewProfilePanel() {
		ScrollPanel scrollPanel = new ScrollPanel();
		HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile</h2>");
		
// TODO: Fix this code to reflect refactoring
//		Image profilePhoto = new Image(loginInfo.getPhotoUrl(), 0, 0, 225, 225);
//		Label nicknameLabel = new Label("Nickname: " + loginInfo.getNickname());
//		Label emailLabel = new Label("Email: " + loginInfo.getEmailAddress());
//		Label headlineLabel = new Label("Headline: " + loginInfo.getHeadline());
//		Label genderLabel = new Label("Gender: " + loginInfo.getGender());
//		Label favouriteFoodLabel = new Label("Favourite Food: " + loginInfo.getFavouriteFood());
//		Label hometownLabel = new Label("Hometown: " + loginInfo.getHometown());
//		Label websiteUrlLabel = new Label("Website: " + loginInfo.getWebsiteUrl());
//		Anchor editProfileLink = new Anchor("Edit Profile");
//		editProfileLink.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				loadEditProfile();
//			}
//		});
//		
//		htmlPanel.add(profilePhoto);
//		htmlPanel.add(nicknameLabel);
//		htmlPanel.add(emailLabel);
//		htmlPanel.add(headlineLabel);
//		htmlPanel.add(genderLabel);
//		htmlPanel.add(favouriteFoodLabel);
//		htmlPanel.add(hometownLabel);
//		htmlPanel.add(websiteUrlLabel);
//		htmlPanel.add(editProfileLink);
		
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}
}
