package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.ui.FoodSorce;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {
	
	private static final VendorListPanel INSTANCE = new VendorListPanel();
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private MapSearchPanel mapSearchPanel;
	private FoodSorce foodSorce;
	
	private ScrollPanel scrollPanel;
	private FlexTable vendorTable = new FlexTable();
	
	private ArrayList<Vendor> allVendors = new ArrayList<Vendor>();
	private ArrayList<Vendor> nearbyVendors = new ArrayList<Vendor>();
	private Vendor selectedVendor;
	private String searchText;
	
//	private static final int REFRESH_INTERVAL = 15000; // milliseconds

	private VendorListPanel() {
		
		GWT.log("VendorListPanel.java: VendorListPanel() constructor");
		// Overall table settings
		vendorTable.addStyleName("vendorList");
		vendorTable.setCellPadding(5);
		
		// Table header settings
		vendorTable.setText(0, 0, "Vendor Name");
		vendorTable.setText(0, 1, "Type of Food");
		vendorTable.setText(0, 2, "Location");
		vendorTable.setText(0, 3, "Quality");
		vendorTable.setText(0, 4, "Cost");
		vendorTable.getColumnFormatter().setWidth(0, "100px");
		vendorTable.getColumnFormatter().setWidth(1, "100px");
		vendorTable.getColumnFormatter().setWidth(2, "400px");
		vendorTable.getColumnFormatter().setWidth(3, "50px");
		vendorTable.getColumnFormatter().setWidth(4, "50px");
		vendorTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		
		// Add panels
		scrollPanel = new ScrollPanel(vendorTable);
		scrollPanel.setHeight("655px");
		this.add(scrollPanel);
		
		// Retrieve and display vendors from server
		fetchAndDisplayVendors();
		
//		// Setup timer to refresh vendor list automatically
//		Timer refreshTimer = new Timer() {
//			@Override
//			public void run() {
//			}
//		};
//		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	
	public static VendorListPanel getInstance() {
		GWT.log("VendorListPanel.java: getInstance");
		return INSTANCE;
	}
	
	public void setFoodSorce(FoodSorce foodSorce) {
		this.foodSorce = foodSorce;
	}
	
	public void setAndDisplayNearbyVendors(List<Vendor> nearbyVendors) {
		GWT.log("VendorListPanel.java: setAndDisplayNearbyVendors");
		this.nearbyVendors.clear();
		this.nearbyVendors.addAll(nearbyVendors);
		displayVendors(nearbyVendors);
	}
	
	private void fetchAndDisplayVendors() {
		vendorService.getVendors(new AsyncCallback<Vendor[]>() {
			public void onFailure(Throwable error) {
				GWT.log("VendorListPanel.java: fetchAndDisplayVendors onFailure", error);
				handleError(error);
			}
			public void onSuccess(Vendor[] result) {
				GWT.log("VendorListPanel.java: fetchAndDisplayVendors onSuccess");
				allVendors.clear();
				allVendors.addAll(Arrays.asList(result));
				mapSearchPanel = MapSearchPanel.getInstance();
				mapSearchPanel.setAllVendors(allVendors);
				setAndDisplayNearbyVendors(allVendors);
			}
		});
	}
	
	private void displayVendors(List<Vendor> vendors) {
		GWT.log("VendorListPanel.java: displayVendors");
		
		// Remove all rows except first header row
		int numRows = vendorTable.getRowCount();
		for (int i = 1; i < numRows; i++)
			vendorTable.removeRow(1);
		
		// Add all vendors to vendorTable
		for (Vendor vendor : vendors)
			displayVendor(vendor);
		
		// Add header style
		vendorTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		
		// Add ClickHandler for displaying VendorInfoPanel
		vendorTable.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				loadVendorInfoPanel(vendorTable.getCellForEvent(event).getRowIndex());
			}
		});
	}

	private void displayVendor(Vendor vendor) {
		// Add the vendor to the table
		int row = vendorTable.getRowCount();
		vendorTable.setText(row, 0, vendor.getName());
		vendorTable.setText(row, 1, vendor.getDescription());
		vendorTable.setText(row, 2, vendor.getLocation());
		// TODO: Low priority task: display these ratings with stars instead of just text.
		if (vendor.getAverageQuality() != -1)
			vendorTable.setText(row, 3, String.valueOf(vendor.getAverageQuality()));
		if (vendor.getAverageCost() != -1)
			vendorTable.setText(row, 4, String.valueOf(vendor.getAverageCost()));
		// Add styles names
		vendorTable.getCellFormatter().addStyleName(row, 0, "vendorListNameColumn");
		vendorTable.getCellFormatter().addStyleName(row, 1, "vendorListTextColumn");
		vendorTable.getCellFormatter().addStyleName(row, 2, "vendorListTextColumn");
		vendorTable.getCellFormatter().addStyleName(row, 3, "vendorListRatingColumn");
		vendorTable.getCellFormatter().addStyleName(row, 4, "vendorListRatingColumn");
	}

	private void loadVendorInfoPanel(int rowIndex) {
		selectedVendor = nearbyVendors.get(rowIndex - 1);
		foodSorce.loadVendorInfoPanel(selectedVendor);
		mapSearchPanel.plotSelectedVendor(selectedVendor);
	}
	
	private void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the VendorListPanel
			// without being logged in is allowed.
		}
		else {
			GWT.log("VendorListPanel.java: handleError", error);
			Window.alert(error.getMessage());
		}
	}

	public ArrayList<Vendor> getAllVendors() {
		return allVendors;
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
	}

}