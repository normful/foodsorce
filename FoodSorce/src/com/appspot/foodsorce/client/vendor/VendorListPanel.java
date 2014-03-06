package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {
	
	private ArrayList<VendorInfo> vendors = new ArrayList<VendorInfo>();
	
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private VendorRatingServiceAsync vendorRatingService = GWT.create(VendorRatingService.class);
	
	private FlexTable vendorFlexTable = new FlexTable();
	
	private static final int REFRESH_INTERVAL = 5000; // milliseconds

	public VendorListPanel() {
		loadVendorList();
	}
	
	private void loadVendorList() {
		// Create table for vendors
		vendorFlexTable.setText(0, 0, "Name");
		vendorFlexTable.setText(0, 1, "Type of Food");
		vendorFlexTable.setText(0, 2, "Quality");
		vendorFlexTable.setText(0, 3, "Cost");
		vendorFlexTable.setText(0, 4, "Location");
		vendorFlexTable.setCellPadding(6);
		
		// Add styles to elements in the vendor list table
		vendorFlexTable.addStyleName("vendorList");
		vendorFlexTable.getRowFormatter().addStyleName(0, "vendorListHeader");
		vendorFlexTable.getCellFormatter().addStyleName(0, 0, "vendorListHeaderText");
		vendorFlexTable.getCellFormatter().addStyleName(0, 1, "vendorListHeaderText");
		vendorFlexTable.getCellFormatter().addStyleName(0, 2, "vendorListHeaderText");
		vendorFlexTable.getCellFormatter().addStyleName(0, 3, "vendorListHeaderText");
		vendorFlexTable.getCellFormatter().addStyleName(0, 4, "vendorListHeaderText");
		
		// Call VendorService RPC should return array of VendorInfo
		loadVendors();

		// Assemble main panel
		add(vendorFlexTable);
		
		// Setup timer to refresh vendor list automatically
		Timer refreshTimer = new Timer() {
			@Override
			public void run() {
				refreshVendorList();
			}
		};
		refreshTimer.scheduleRepeating(REFRESH_INTERVAL);

		// TODO: Set focus to vendor search text box.
		// Move cursor focus to the input box.
//		newSymbolTextBox.setFocus(true);
		
		// TODO: Add key handler for vendor search text box.
		// Listen for keyboard events in the input box.
//		newSymbolTextBox.addKeyDownHandler(new KeyDownHandler() {
//			public void onKeyDown(KeyDownEvent event) {
//				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
//					addStock();
//				}
//			}
//		});
	}
	
	private void loadVendors() {
		vendorService.getVendorInfos(new AsyncCallback<VendorInfo[]>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}
			public void onSuccess(VendorInfo[] vendorInfos) {
				displayVendors(vendorInfos);
			}
		});
	}
	
	public void handleError(Throwable error) {
		Window.alert(error.getMessage());
	}
	
	private void displayVendors(VendorInfo[] vendorInfos) {
		for (VendorInfo vendorInfo : vendorInfos)
			displayVendor(vendorInfo);
	}

	private void displayVendor(final VendorInfo vendorInfo) {
		// Add the vendor to the table
		int row = vendorFlexTable.getRowCount();
		vendors.add(vendorInfo);
		vendorFlexTable.setText(row, 0, vendorInfo.getName());
		vendorFlexTable.setText(row, 1, vendorInfo.getFoodType());
		// TODO: Low priority task: display these ratings with stars instead of just text.
		vendorFlexTable.setText(row, 2, String.valueOf(vendorInfo.getAverageQuality()));
		vendorFlexTable.setText(row, 3, String.valueOf(vendorInfo.getAverageCost()));
		vendorFlexTable.setText(row, 4, vendorInfo.getLocation());
		vendorFlexTable.getCellFormatter().addStyleName(row, 0, "vendorListTextColumn");
		vendorFlexTable.getCellFormatter().addStyleName(row, 1, "vendorListTextColumn");
		vendorFlexTable.getCellFormatter().addStyleName(row, 2, "vendorListRatingColumn");
		vendorFlexTable.getCellFormatter().addStyleName(row, 3, "vendorListRatingColumn");
		vendorFlexTable.getCellFormatter().addStyleName(row, 4, "vendorListTextColumn");

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

//	@SuppressWarnings("deprecation")
//	private void updateTable(StockPrice[] prices) {
//		// Auto-generated method stub
//		for (int i = 0; i < prices.length; i++) {
//			updateTable(prices[i]);
//		}
//		
//		// Display timestamp showing last refresh.
//		lastUpdatedLabel.setText("Last update : " + DateTimeFormat.getMediumDateTimeFormat().format(new Date()));
//		
//		// Clear any errors
//		errorMsgLabel.setVisible(false);
//	}
//
//	/**
//	 * Update a single row in the stock table.
//	 *
//	 * @param price Stock data for a single row.
//	 */
//	private void updateTable(StockPrice price) {
//		// Make sure the stock is still in the stock table.
//		if (!stocks.contains(price.getSymbol())) {
//			return;
//		}
//
//		int row = stocks.indexOf(price.getSymbol()) + 1;
//
//		// Format the data in the Price and Change fields.
//		String priceText = NumberFormat.getFormat("#,##0.00").format(
//				price.getPrice());
//		NumberFormat changeFormat = NumberFormat.getFormat("+#,##0.00;-#,##0.00");
//		String changeText = changeFormat.format(price.getChange());
//		String changePercentText = changeFormat.format(price.getChangePercent());
//
//		// Populate the Price and Change fields with new data.
//		stocksFlexTable.setText(row, 1, priceText);
//		Label changeWidget = (Label) stocksFlexTable.getWidget(row, 2);
//		changeWidget.setText(changeText + " (" + changePercentText + "%)");
//
//		// Change the color of text in the Change field based on its value
//		String changeStyleName = "noChange";
//		if (price.getChangePercent() < -0.1f) {
//			changeStyleName = "negativeChange";
//		} else if (price.getChangePercent() > 0.1f) {
//			changeStyleName = "positiveChange";
//		}
//		
//		changeWidget.setStyleName(changeStyleName);
//	}
//	
//	
}