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
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
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

public class ProfilePanel extends HorizontalPanel {

	private String userEmail;
	private Profile profile;
	private static final ProfilePanel INSTANCE = new ProfilePanel();
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	private static final int MAX_TRIES = 10;
	private int getProfileTryCount;

	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();

	private ScrollPanel leftScrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile</h2>");

	private static final String DEFAULT_PHOTO_URL = "images/unknown_user.jpeg";
	private String photoUrl = DEFAULT_PHOTO_URL;
	private Image profilePhoto = new Image(photoUrl, 0, 0, 255, 255);

	private Button importFacebookPhotoButton = new Button("Import Facebook photo");
	private TextBox facebookUsernameTextBox = new TextBox();
	private VerticalPanel importFacebookPhotoPanel = new VerticalPanel();
	private Button submitFacebookUsernameButton = new Button("Submit");

	private FlexTable settingsTable = new FlexTable();
	private VerticalPanel leftProfilePanel = new VerticalPanel();

	private HTMLPanel favouriteVendorsHTML = new HTMLPanel("<h2>Favourites</h2>");
	private FlexTable favouriteVendorTable = new FlexTable();
	private ArrayList<Vendor>favouritedVendors = new ArrayList<Vendor>();
	private ScrollPanel rightScrollPanel = new ScrollPanel();

	private Anchor editProfileLink = new Anchor("Edit Profile");
	private HashMap<String, TextBox> editBoxMap = new HashMap<String, TextBox>();
	private HashMap<String, String> settingsMap = new HashMap<String, String>();
	private Button submitSettingsButton = new Button("Submit");

	private ProfilePanel() {
		reloadLayout();
		leftScrollPanel.add(htmlPanel);
		leftProfilePanel.add(leftScrollPanel);
		add(leftProfilePanel);

		createFavouriteVendorTable();
		favouriteVendorsHTML.add(favouriteVendorTable);
		rightScrollPanel.add(favouriteVendorsHTML);
		add(rightScrollPanel);
	}

	public static ProfilePanel getInstance() {
		GWT.log("ProfilePanel.java: getInstance");
		return INSTANCE;
	}

	private void reloadLayout() {
		htmlPanel.clear();
		htmlPanel.add(profilePhoto);
		createImportFacebookPhotoUI();
		htmlPanel.add(settingsTable);
	}
	
	private void createImportFacebookPhotoUI() {
		if (photoUrl.equals(DEFAULT_PHOTO_URL)) {
			importFacebookPhotoButton.addClickHandler(new ClickHandler() {
				@Override
				public void onClick(ClickEvent event) {
					htmlPanel.remove(importFacebookPhotoButton);
					loadImportFacebookPhotoPanel();
				}
			});
			htmlPanel.add(new HTML("<br>"));
			htmlPanel.add(importFacebookPhotoButton);
			htmlPanel.add(importFacebookPhotoPanel);
		} else {
			htmlPanel.remove(importFacebookPhotoButton);
			htmlPanel.remove(importFacebookPhotoPanel);
		}
	}

	private void loadImportFacebookPhotoPanel() {
		facebookUsernameTextBox.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				boolean enterPressed = KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode();
				if (enterPressed)
					importFacebookPhoto();
			}
		});
		facebookUsernameTextBox.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				facebookUsernameTextBox.selectAll();
			}});
		facebookUsernameTextBox.setText("Enter facebook username");
		submitFacebookUsernameButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				importFacebookPhoto();
			}
		});
		importFacebookPhotoPanel.add(facebookUsernameTextBox);
		importFacebookPhotoPanel.add(submitFacebookUsernameButton);
		importFacebookPhotoPanel.add(new HTML("<br>"));
	}

	private void importFacebookPhoto() {
		htmlPanel.remove(importFacebookPhotoPanel);
		String photoURL = createFacebookPhotoUrl(facebookUsernameTextBox.getText());
		profileService.getGraphUrl(photoURL, new AsyncCallback<String>() {
			public void onSuccess(String facebookPhotoUrl) {
				if (facebookPhotoUrl != null) {
					setFacebookPhoto(facebookPhotoUrl);
					reloadLayout();
				}
			}
			public void onFailure(Throwable caught) {
				// Do nothing. Keep default photo.
			}
		});
	}

	private String createFacebookPhotoUrl(String facebookUsername) {
		return ("http://graph.facebook.com/" +
				facebookUsername.replace(' ', '.').toLowerCase() +
				"/picture/?type=large");
	}

	private void setFacebookPhoto(String facebookPhotoUrl) {
		photoUrl = facebookPhotoUrl;
		htmlPanel.remove(profilePhoto);
		profilePhoto = new Image(facebookPhotoUrl, 0, 0, 255, 255);
		profile.setPhotoUrl(facebookPhotoUrl);
		updateProfile();
		reloadLayout();
	}

	public void getProfile() {
		if (getProfileTryCount++ > MAX_TRIES)
			return;
		profileService.getProfile(userEmail, new AsyncCallback<Profile>() {
			public void onSuccess(Profile result) {
				profile = result;
				createImportFacebookPhotoUI();
				setFacebookPhoto(result.getPhotoUrl());
				settingsMap.putAll(result.getSettings());
				loadViewSettingsTable();
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

	private void loadViewSettingsTable() {
		GWT.log("ProfilePanel.java: loadViewLayout()");
		htmlPanel.remove(submitSettingsButton);

		settingsTable.removeAllRows();
		settingsTable.setCellPadding(5);
		settingsTable.getColumnFormatter().setWidth(0, "125px");
		settingsTable.getColumnFormatter().setWidth(1, "200px");
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
			} else {
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
				loadEditSettingsTable();
			}
		});

		htmlPanel.add(editProfileLink);
	}

	private void loadEditSettingsTable() {
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

		submitSettingsButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				saveNewSettings();
				updateProfile();
				loadViewSettingsTable();
			}
		});

		htmlPanel.add(submitSettingsButton);
	}

	private void saveNewSettings() {
		for (Map.Entry<String, TextBox> entry : editBoxMap.entrySet()) {
			settingsMap.put(entry.getKey(), entry.getValue().getText());
			if (entry.getKey().equals("searchText"))
				VendorListPanel.getInstance().setSearchText(entry.getValue().getText());
		}
		profile.setSettings(settingsMap);
	}

	private void updateProfile() {
		GWT.log("ProfilePanel.java: updateProfile()");
		profileService.updateProfile(userEmail, profile, new AsyncCallback<Void>() {
			@Override
			public void onSuccess(Void result) {
				loadViewSettingsTable();
			}
			@Override
			public void onFailure(Throwable caught) {
				loadViewSettingsTable();
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

	public void setFavouriteVendors() {
		if (userEmail == null || userEmail.isEmpty())
			return;
		vendorListPanel = VendorListPanel.getInstance();
		favouritedVendors.clear();
		ArrayList<Vendor> allVendors = vendorListPanel.getAllVendors();
		for (Vendor vendor : allVendors) {
			if (vendor.getFavouriters() != null) {
				for (String favouriter : vendor.getFavouriters()) {
					if (userEmail.equals(favouriter) &&
						!favouritedVendors.contains(vendor)) {
						favouritedVendors.add(vendor);
					}
				}
			}
		}
		displayFavouriteVendors(favouritedVendors);
	}

	private void createFavouriteVendorTable() {
		favouriteVendorTable.addStyleName("vendorList");
		favouriteVendorTable.setCellPadding(5);
		favouriteVendorTable.getColumnFormatter().setWidth(0, "300px");
		favouriteVendorTable.getRowFormatter().addStyleName(0, "vendorListHeader");
	}

	private void displayFavouriteVendors(ArrayList<Vendor> vendors) {
		favouriteVendorTable.removeAllRows();
		for (Vendor vendor : vendors)
			displayFavouriteVendor(vendor);
	}

	private void displayFavouriteVendor(Vendor vendor) {
		int row = favouriteVendorTable.getRowCount();
		favouriteVendorTable.setText(row, 0, vendor.getName());
		favouriteVendorTable.getCellFormatter().addStyleName(row, 0, "vendorListNameColumn");
	}
}