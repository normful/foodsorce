package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.Vendor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {
	
	private FlexTable vendorTable = new FlexTable();
	
	private List<Vendor> vendors = new ArrayList<Vendor>();
	
	// Not ready yet
//	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
//	private VendorRatingServiceAsync vendorRatingService = GWT.create(VendorRatingService.class);
	
	private static final int REFRESH_INTERVAL = 5000; // milliseconds

	public VendorListPanel() {
		loadVendorList();
	}
	
	private void loadVendorList() {
		// Overall table settings
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
		
		// Call VendorService RPC should return array of VendorInfo
		loadVendors();

		// Add vendorTable
		add(vendorTable);
		
		// Setup timer to refresh vendor list automatically
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshVendorList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);
	}
	
	private void loadVendors() {
		// Not ready yet
//		vendorService.getVendors(new AsyncCallback<Vendor[]>() {
//			public void onFailure(Throwable error) {
//				handleError(error);
//			}
//			public void onSuccess(Vendor[] vendors) {
//				displayVendors(vendors);
//			}
//		});
	}
	
	public void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the VendorListPanel
			// without being logged in is allowed.
		}
		else
			Window.alert(error.getMessage());
	}
	
	private void displayVendors(Vendor[] vendors) {
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
		vendorTable.getCellFormatter().addStyleName(row, 0, "vendorListTextColumn");
		vendorTable.getCellFormatter().addStyleName(row, 1, "vendorListTextColumn");
		vendorTable.getCellFormatter().addStyleName(row, 2, "vendorListRatingColumn");
		vendorTable.getCellFormatter().addStyleName(row, 3, "vendorListRatingColumn");
		vendorTable.getCellFormatter().addStyleName(row, 4, "vendorListTextColumn");

		refreshVendorList();
	}
	
	private void refreshVendorList() {
		
		// TODO: Implement refreshing of VendorInfo averageQuality and averageCost ratings periodically
		// Initialize the service proxy.
//		if (vendorRatingService == null) {
//			vendorRatingService = GWT.create(VendorRatingService.class);
//		}

//		// Set up the callback object.
//		AsyncCallback<StockPrice[]> callback = new AsyncCallback<StockPrice[]>() {
//			public void onFailure(Throwable caught) {
//				// If the stock code is in the list of delisted codes, display an error message.
//				String details = caught.getMessage();
//				if (caught instanceof DelistedException) {
//					details = "Company '" + ((DelistedException)caught).getSymbol() + "' was delisted";
//				}
//				errorMsgLabel.setText("Error: " + details);
//				errorMsgLabel.setVisible(true);
//			}
//
//			public void onSuccess(StockPrice[] result) {
//				updateTable(result);
//			}
//		};
//
//		// Make the call to the stock price service.
//		stockPriceSvc.getPrices(stocks.toArray(new String[0]), callback);
	}


}