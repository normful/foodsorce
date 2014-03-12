package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {
	
	private static final VendorListPanel INSTANCE = new VendorListPanel();
	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	
	private ScrollPanel scrollPanel;
	private FlexTable vendorTable;
	private List<Vendor> allVendors;
	private List<Vendor> matchingVendors;
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
//	private static final int REFRESH_INTERVAL = 15000; // milliseconds

	private VendorListPanel() {
		allVendors = new ArrayList<Vendor>();
		matchingVendors = new ArrayList<Vendor>();
		
		System.out.println("VendorListPanel.java: VendorListPanel() constructor");
		// Overall table settings
		vendorTable = new FlexTable();
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
		System.out.println("VendorListPanel.java: getInstance");
		return INSTANCE;
	}
	
	public void setAndDisplayMatchingVendors(List<Vendor> matchingVendors) {
		System.out.println("VendorListPanel.java: setAndDisplayMatchingVendors");
		this.matchingVendors.clear();
		this.matchingVendors.addAll(matchingVendors);
		displayVendors(matchingVendors);
	}
	
	private void fetchAndDisplayVendors() {
		vendorService.getVendors(new AsyncCallback<Vendor[]>() {
			public void onFailure(Throwable error) {
				System.err.println("VendorListPanel.java: fetchAndDisplayVendors onFailure");
				handleError(error);
			}
			public void onSuccess(Vendor[] result) {
				System.out.println("VendorListPanel.java: fetchAndDisplayVendors onSuccess");
				allVendors.clear();
				allVendors.addAll(Arrays.asList(result));
				mapSearchPanel.setAllVendors(allVendors);
				setAndDisplayMatchingVendors(allVendors);
			}
		});
	}
	
	private void displayVendors(List<Vendor> vendors) {
		System.out.println("VendorListPanel.java: displayVendors");
		
		// Remove all rows except first header row
		int numRows = vendorTable.getRowCount();
		for (int i = 1; i < numRows; i++)
			vendorTable.removeRow(1);
		
		// Add all vendors to vendorTable
		for (Vendor vendor : vendors)
			displayVendor(vendor);
		
		// Add style names
		vendorTable.getColumnFormatter().addStyleName(0, "vendorListNameColumn");
		vendorTable.getColumnFormatter().addStyleName(1, "vendorListTextColumn");
		vendorTable.getColumnFormatter().addStyleName(2, "vendorListTextColumn");
		vendorTable.getColumnFormatter().addStyleName(3, "vendorListRatingColumn");
		vendorTable.getColumnFormatter().addStyleName(4, "vendorListRatingColumn");
		vendorTable.getRowFormatter().addStyleName(0, "vendorListHeader");
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
	}

	private void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the VendorListPanel
			// without being logged in is allowed.
		}
		else
			Window.alert(error.getMessage());
	}

}