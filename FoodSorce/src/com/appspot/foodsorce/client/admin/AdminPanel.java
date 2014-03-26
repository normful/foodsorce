package com.appspot.foodsorce.client.admin;

import java.util.Arrays;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.client.profile.ProfileServiceAsync;
import com.appspot.foodsorce.client.vendor.VendorService;
import com.appspot.foodsorce.client.vendor.VendorServiceAsync;
import com.appspot.foodsorce.shared.Profile;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminPanel extends VerticalPanel {
	
	private Button importDataButton = new Button("Import Data");
	private VancouverDataServiceAsync dataService;
	
	private ProfileServiceAsync profileService = GWT.create(ProfileService.class);
	private ScrollPanel scrollPanel;
	private FlexTable profileTable = new FlexTable();
	
	public AdminPanel() {
		HTMLPanel htmlPanel = new HTMLPanel("<h2>Administrator Console</h2><br>"
				+ "This button imports Vancouver food vendor data into FoodSorce.<br><br>");
		
		// TODO: Display a list of Profiles

		
		importDataButton.addStyleName("importDataButton");
		importDataButton.setWidth("100px");
		importDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				callImportData();
			}
		});
		
		this.add(htmlPanel);
		this.add(importDataButton);
		createProfileList();
		
	}
	
	private void createProfileList() {
		profileTable.addStyleName("vendorList");
		profileTable.setCellPadding(2);


		profileTable.setText(0, 0, "Email");
		profileTable.setText(0, 1, "Delete User");
		profileTable.getColumnFormatter().setWidth(0, "500px");
		profileTable.getColumnFormatter().setWidth(1, "300px");

		profileTable.getRowFormatter().addStyleName(0, "vendorListHeader");


		scrollPanel = new ScrollPanel(profileTable);
		scrollPanel.setHeight("655px");
		this.add(scrollPanel);
		
		fetchAndDisplayProfiles();
	}
	
	private void callImportData() {
		dataService = GWT.create(VancouverDataService.class);
		dataService.importData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Failed to import data: " + error.toString());
			}
			public void onSuccess(Void voidResult) {
				Window.alert("Successfully imported data.");
			}
		});
	}
	
	private void fetchAndDisplayProfiles() {
		profileService.getAllProfiles(new AsyncCallback<Profile[]>() {
			public void onFailure(Throwable error) {
				GWT.log("AdminPanel.java: fetchAndDisplayProfiles onFailure", error);
				handleError(error);
			}
			public void onSuccess(Profile[] result) {
				GWT.log("AdminPanel.java: fetchAndDisplayProfiles onSuccess");
				displayProfiles(result);
			}

		});
	}
	
	private void displayProfiles(Profile[] profiles) {
		
		GWT.log("ProfilePanel.java: displayProfiles");

		// Remove all rows except first header row
		int numRows = profileTable.getRowCount();
		for (int i = 1; i < numRows; i++)
			profileTable.removeRow(1);

		// Add all vendors to vendorTable
		for (Profile profile : profiles)
			displayProfile(profile);

		// Add header style
		profileTable.getRowFormatter().addStyleName(0, "vendorListHeader");


	}
	
	private void displayProfile(Profile profile) {
		// Add the vendor to the table
		int row = profileTable.getRowCount();
		profileTable.setText(row, 0, profile.getUserEmail());
//		profileTable.setText(row, 1, profile.getDescription());

		// Add styles names
		profileTable.getCellFormatter().addStyleName(row, 0, "vendorListNameColumn");
	}
	
	
	private void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the AdminPanel
			// without being logged in is allowed.
		}
		else {
			GWT.log("AdminPanel.java: handleError", error);
			Window.alert(error.getMessage());
		}
	}
	
}
