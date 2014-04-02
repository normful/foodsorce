package com.appspot.foodsorce.client.profile;

import java.util.HashMap;
import java.util.Map;

import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ProfilePanel extends VerticalPanel {

	private String userEmail;
	private Profile profile;
	private static final ProfilePanel INSTANCE = new ProfilePanel();
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	private static final int MAX_TRIES = 10;
	private int getProfileTryCount;

	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();

	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile</h2>");
	
	private String defaultPhotoUrl = "images/unknown_user.jpeg";
	private Image profilePhoto = new Image(defaultPhotoUrl, 0, 0, 255, 255);
	
	private Button importFbPicButton = new Button("Import Facebook photo");
	private TextBox fbUsernameTextBox = new TextBox();
	private HorizontalPanel fbLoginPanel = new HorizontalPanel();

	private FlexTable settingsTable = new FlexTable();
	private Anchor editProfileLink = new Anchor("Edit Profile");
	private HashMap<String, TextBox> editBoxMap = new HashMap<String, TextBox>();
	private HashMap<String, String> settingsMap = new HashMap<String, String>();
	private Button submitButton = new Button("Submit");

	private ProfilePanel() {
		htmlPanel.add(profilePhoto);
		
		importFbPicButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				importFacebookPhoto();
			}
		});
		
		fbLoginPanel.add(importFbPicButton);
		fbLoginPanel.add(fbUsernameTextBox);
		fbLoginPanel.add(new HTML("Facebook username:"));
		htmlPanel.add(fbLoginPanel);
		
		htmlPanel.add(settingsTable);
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}

	private void importFacebookPhoto() {
		String photoURL = createFacebookPhotoUrl(fbUsernameTextBox.getText());
		profileService.getGraphUrl(photoURL, new AsyncCallback<String>() {
			public void onSuccess(String result) {
				if (result != null)
					setFacebookPhoto(result);
			}
			public void onFailure(Throwable caught) {
				// Do nothing. Keep default photo.
			}
		});
	}

	public static ProfilePanel getInstance() {
		GWT.log("ProfilePanel.java: getInstance");
		return INSTANCE;
	}

	private void setFacebookPhoto(String result) {
		profile.setPhotoUrl(result);
		updateProfile();
		profilePhoto = new Image(profile.getPhotoUrl(), 0, 0, 255, 255);
//		try {
//			htmlPanel.clear();
//			htmlPanel.add(profilePhoto);
//			htmlPanel.add(fbLoginPanel);
//			htmlPanel.add(settingsTable);
//		} catch (Throwable e) {
//			profilePhoto = new Image(defaultPhotoUrl, 0, 0, 255, 255);
//		}
	}
	
	public void getProfile() {
		if (getProfileTryCount++ > MAX_TRIES)
			return;
		profileService.getProfile(userEmail, new AsyncCallback<Profile>() {
			public void onSuccess(Profile result) {
				profile = result;
				settingsMap.putAll(result.getSettings());
				loadViewLayout();
				mapSearchPanel.setSearchDistance(result.getSettings().get("searchDistance"));
				mapSearchPanel.updateAndPlotNearbyVendors();
				vendorListPanel.setSearchText(result.getSettings().get("searchText"));
				vendorListPanel.searchVendor();
			}
			public void onFailure(Throwable error) {
				GWT.log("ProfilePanel.java: getProfile onFailure", error);
				getProfile();
			}
		});
	}

	private void loadViewLayout() {
		GWT.log("ProfilePanel.java: loadViewLayout()");
		htmlPanel.remove(submitButton);
		settingsTable.removeAllRows();

//		try {
//			htmlPanel.clear();
//			profilePhoto = new Image(profile.getPhotoUrl(), 0, 0, 255, 255);
//			htmlPanel.add(profilePhoto);
//			htmlPanel.add(fbLoginPanel);
//			htmlPanel.add(settingsTable);
//		} catch (Throwable e) {
//			profilePhoto = new Image(defaultPhotoUrl, 0, 0, 255, 255);
//		}

		settingsTable.setCellPadding(5);
		settingsTable.getColumnFormatter().setWidth(0, "125px");
		settingsTable.getColumnFormatter().setWidth(1, "400px");
		settingsTable.setText(0, 0, "Email");
		settingsTable.setText(0, 1, userEmail);
		settingsTable.getCellFormatter().setHeight(0, 0, "20px");
		settingsTable.getCellFormatter().setAlignment(0, 0, ALIGN_LEFT, ALIGN_MIDDLE);
		settingsTable.getCellFormatter().setAlignment(0, 1, ALIGN_LEFT, ALIGN_MIDDLE);
		settingsTable.getCellFormatter().setStyleName(0, 0, "profilePanelSettingsTableKeys");
		settingsTable.getCellFormatter().setStyleName(0, 1, "profilePanelSettingsTableValues");
		
		for (Map.Entry<String, String> setting : settingsMap.entrySet()) {
			int row = settingsTable.getRowCount();
			if (setting.getKey().equals("searchText")) {
				settingsTable.setText(row, 0, "Search Text");
			} else if (setting.getKey().equals("searchDistance")) {
				settingsTable.setText(row, 0, "Search Distance");
			} else if (!setting.getKey().equals("photoUrl")) {
				// All other settings
				settingsTable.setText(row, 0, setting.getKey());
			}
			settingsTable.setText(row, 1, setting.getValue());
			settingsTable.getCellFormatter().setHeight(row, 0, "20px");
			settingsTable.getCellFormatter().setAlignment(row, 0, ALIGN_LEFT, ALIGN_MIDDLE);
			settingsTable.getCellFormatter().setAlignment(row, 1, ALIGN_LEFT, ALIGN_MIDDLE);
			settingsTable.getCellFormatter().setStyleName(row, 0, "profilePanelSettingsTableKeys");
			settingsTable.getCellFormatter().setStyleName(row, 1, "profilePanelSettingsTableValues");
		}

		editProfileLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadEditLayout();
			}
		});

		htmlPanel.add(editProfileLink);
	}
	
	private String createFacebookPhotoUrl(String facebookUsername) {
		return ("http://graph.facebook.com/" +
				facebookUsername.replace(' ', '.').toLowerCase() +
				"/picture/?type=large");
	}

	private void loadEditLayout() {
		htmlPanel.remove(editProfileLink);
		settingsTable.removeAllRows();
		for (Map.Entry<String, String> setting : settingsMap.entrySet()) {
			int row = settingsTable.getRowCount();
			final TextBox editBox = new TextBox();
			editBox.setText(setting.getValue());
			editBox.setWidth("400px");
			editBox.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					editBox.selectAll();
				}});
			settingsTable.setText(row, 0, setting.getKey());
			settingsTable.setWidget(row, 1, editBox);
			editBoxMap.put(setting.getKey(), editBox);
		}

		submitButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveNewSettings();
				updateProfile();
				loadViewLayout();
			}
		});

		htmlPanel.add(submitButton);
	}

	private void saveNewSettings() {
		for (Map.Entry<String, TextBox> entry : editBoxMap.entrySet())
			settingsMap.put(entry.getKey(), entry.getValue().getText());
		profile.setSettings(settingsMap);
	}

	private void updateProfile() {
		GWT.log("ProfilePanel.java: updateProfile()");
		profileService.updateProfile(userEmail, profile, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadViewLayout();
			}
			@Override
			public void onFailure(Throwable caught) {
				loadViewLayout();
			}
		});
	}

	public void setUserEmail(String userEmail) {
		if (userEmail != null && !userEmail.isEmpty())
			this.userEmail = userEmail;
	}

	public void setSearchDistance(String searchDistance) {
		GWT.log("ProfilePanel.java: setSearchDistance()");
		if (profile != null) {
			settingsMap.put("searchDistance", searchDistance);
			profile.setSettings(settingsMap);
			updateProfile();
		}
	}

	public void setSearchText(String searchText) {
		GWT.log("ProfilePanel.java: setSearchText()");
		if (profile != null) {
			settingsMap.put("searchText", searchText);
			profile.setSettings(settingsMap);
			updateProfile();
		}
	}

}