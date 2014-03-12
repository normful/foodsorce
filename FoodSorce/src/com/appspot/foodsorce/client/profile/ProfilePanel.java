package com.appspot.foodsorce.client.profile;

import java.util.Map;

import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfilePanel extends VerticalPanel {
	
	private String userEmail = "";
	private Profile profile = new Profile();
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile</h2>");
	private Image profilePhoto = new Image("images/unknown_user.jpeg", 0, 0, 255, 255);
	private FlexTable settingsTable = new FlexTable();
	private Anchor editProfileLink = new Anchor("Edit Profile");
	private Button submitButton;
	
	public ProfilePanel(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty())
			this.userEmail = userEmail;
		
		settingsTable.getColumnFormatter().setWidth(0, "125px");
		settingsTable.getColumnFormatter().setWidth(1, "300px");
		settingsTable.getColumnFormatter().setStyleName(0, "profileGridKeys");
		settingsTable.getColumnFormatter().setStyleName(1, "profileGridValues");
		
		htmlPanel.add(profilePhoto);
		htmlPanel.add(settingsTable);
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
		
		getProfile();
	}
	
	private void getProfile() {
		profileService.getProfile(userEmail, new AsyncCallback<Profile>() {
			public void onFailure(Throwable error) {
				System.err.println("ViewProfilePanel.java: onFailure");
			}
			public void onSuccess(Profile result) {
				System.err.println("ViewProfilePanel.java: onSuccess");
				profile = result;
				loadViewLayout();
			}
		});
	}
	
	private void loadViewLayout() {
	
		profilePhoto = new Image(profile.getSettings().get("photoUrl"), 0, 0, 255, 255);
		
		for (Map.Entry<String, String> entry : profile.getSettings().entrySet()) {
			if (!entry.getKey().equals("photoUrl")) {
				int row = settingsTable.getRowCount();
				settingsTable.setText(row, 0, entry.getKey());
				settingsTable.setText(row, 1, entry.getValue());
			}
		}
		
		editProfileLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadEditLayout();
			}
		});
		
		htmlPanel.add(editProfileLink);
	}
	

	private void loadEditLayout() {
		htmlPanel.remove(editProfileLink);
		
//		grid.setWidget(1, 1, headlineField);
//		grid.setWidget(2, 1, genderField);
//		grid.setWidget(3, 1, favouriteFoodField);
//		grid.setWidget(4, 1, hometownField);
//		grid.setWidget(5, 1, websiteUrlField);
		
		submitButton = new Button();
		
		htmlPanel.add(submitButton);
	}
}
