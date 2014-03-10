package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
//import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {
	
	private ScrollPanel scrollPanel;
	private FlexTable vendorTable;
	private List<Vendor> vendors = new ArrayList<Vendor>();
	private VendorServiceAsync vendorService;
//	private static final int REFRESH_INTERVAL = 15000; // milliseconds

	public VendorListPanel() {
		// Overall table settings
		vendorTable = new FlexTable();
		vendorTable.addStyleName("vendorList");
		vendorTable.setCellPadding(5);
		
		// Table header settings
		vendorTable.setText(0, 0, "Name");
		vendorTable.setText(0, 1, "Type of Food");
		vendorTable.setText(0, 2, "Quality");
		vendorTable.setText(0, 3, "Cost");
		vendorTable.setText(0, 4, "Location");
		vendorTable.getColumnFormatter().setWidth(0, "100px");
		vendorTable.getColumnFormatter().setWidth(1, "100px");
		vendorTable.getColumnFormatter().setWidth(2, "100px");
		vendorTable.getColumnFormatter().setWidth(3, "100px");
		vendorTable.getColumnFormatter().setWidth(4, "200px");
		vendorTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		vendorTable.getCellFormatter().addStyleName(0, 0, "vendorListHeaderText");
		vendorTable.getCellFormatter().addStyleName(0, 1, "vendorListHeaderText");
		vendorTable.getCellFormatter().addStyleName(0, 2, "vendorListHeaderText");
		vendorTable.getCellFormatter().addStyleName(0, 3, "vendorListHeaderText");
		vendorTable.getCellFormatter().addStyleName(0, 4, "vendorListHeaderText");
		
		// Retrieve and display vendors from server
		vendorService = GWT.create(VendorService.class);
		loadVendors();

		// Add vendorTable
		scrollPanel = new ScrollPanel(vendorTable);
		scrollPanel.setHeight("600px");
		this.add(scrollPanel);
		
//		// Setup timer to refresh vendor list automatically
//		Timer refreshTimer = new Timer() {
//			@Override
//			public void run() {
//			}
//		};
//		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	
	private void loadVendors() {
		vendorService.getVendors(new AsyncCallback<Vendor[]>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(Vendor[] result) {
				displayVendors(result);
			}
		});
	}
	
	private void displayVendors(Vendor[] vendors) {
		System.out.println("VendorListPanel.java: displayVendors");
		for (Vendor vendor : vendors)
			displayVendor(vendor);
	}

	private void displayVendor(final Vendor vendor) {
		// Add the vendor to the table
		int row = vendorTable.getRowCount();
		vendors.add(vendor);
		vendorTable.setText(row, 0, vendor.getName());
		vendorTable.setText(row, 1, vendor.getDescription());
		// TODO: Low priority task: display these ratings with stars instead of just text.
		vendorTable.setText(row, 2, String.valueOf(vendor.getAverageQuality()));
		vendorTable.setText(row, 3, String.valueOf(vendor.getAverageCost()));
		vendorTable.setText(row, 4, vendor.getLocation());
		vendorTable.getCellFormatter().addStyleName(row, 0, "vendorListNameColumn");
		vendorTable.getCellFormatter().addStyleName(row, 1, "vendorListTextColumn");
		vendorTable.getCellFormatter().addStyleName(row, 2, "vendorListRatingColumn");
		vendorTable.getCellFormatter().addStyleName(row, 3, "vendorListRatingColumn");
		vendorTable.getCellFormatter().addStyleName(row, 4, "vendorListTextColumn");
	}

	public void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the VendorListPanel
			// without being logged in is allowed.
		}
		else
			Window.alert(error.getMessage());
	}
	
}