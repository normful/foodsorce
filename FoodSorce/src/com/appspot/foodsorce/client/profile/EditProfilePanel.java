package com.appspot.foodsorce.client.profile;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class EditProfilePanel extends VerticalPanel {

	private String userEmail = "";
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("<h2>Edit Profile</h2>");
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);	
	
	public EditProfilePanel(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty())
			this.userEmail = userEmail;
		
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}

}
