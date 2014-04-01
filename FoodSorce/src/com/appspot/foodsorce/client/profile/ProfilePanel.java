package com.appspot.foodsorce.client.profile;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.appspot.foodsorce.shared.Profile;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
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
	private FlexTable settingsTable = new FlexTable();
	
	private Anchor editProfileLink = new Anchor("Edit Profile");
	private HashMap<String, TextBox> editBoxMap = new HashMap<String, TextBox>();
	private HashMap<String, String> settingsMap = new HashMap<String, String>();
	private Button submitButton = new Button("Submit");
	
	private ProfilePanel() {
		settingsTable.getColumnFormatter().setWidth(0, "125px");
		settingsTable.getColumnFormatter().setWidth(1, "400px");
		settingsTable.getColumnFormatter().setStyleName(0, "profileGridKeys");
		settingsTable.getColumnFormatter().setStyleName(1, "profileGridValues");
		
		htmlPanel.add(profilePhoto);
		htmlPanel.add(settingsTable);
		htmlPanel.add(favouriteVendors);
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}
	
	public static ProfilePanel getInstance() {
		GWT.log("ProfilePanel.java: getInstance");
		return INSTANCE;
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
	
	private String setFavouriteVendors() {
		String favouriteVendors;
		ArrayList<Vendor> allVendors = vendorListPanel.getAllVendors();
		for (Vendor vendor : allvendors) {
			for (String userEmailOfFavourite : vendor.getFavourites()) {
				if (userEmail == userEmailOfFavourite) {
			}
		}
		}
	}
	
	private void loadViewLayout() {
		GWT.log("ProfilePanel.java: loadViewLayout()");
		htmlPanel.remove(submitButton);
		settingsTable.removeAllRows();

		try {
			profilePhoto = new Image(profile.getSettings().get("photoUrl"), 0, 0, 255, 255);
		} catch (Throwable e) {
			profilePhoto = new Image(defaultPhotoUrl, 0, 0, 255, 255);
		}

		settingsTable.setText(0, 0, "Email");
		settingsTable.setText(0, 1, userEmail);
		
		for (Map.Entry<String, String> setting : settingsMap.entrySet()) {
			if (setting.getKey().equals("searchText")) {
				int row = settingsTable.getRowCount();
				settingsTable.setText(row, 0, "Search Text");
				settingsTable.setText(row, 1, setting.getValue());
			} else if (setting.getKey().equals("searchDistance")) {
				int row = settingsTable.getRowCount();
				settingsTable.setText(row, 0, "Search Distance");
				settingsTable.setText(row, 1, setting.getValue());
			} else if (!setting.getKey().equals("photoUrl")) {
				// All other settings
				int row = settingsTable.getRowCount();
				settingsTable.setText(row, 0, setting.getKey());
				settingsTable.setText(row, 1, setting.getValue());
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
		settingsTable.removeAllRows();
		
		for (Map.Entry<String, String> setting : settingsMap.entrySet()) {
			if (!setting.getKey().equals("photoUrl")) {
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