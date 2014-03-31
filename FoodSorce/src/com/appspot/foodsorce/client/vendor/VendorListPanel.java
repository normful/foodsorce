package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.profile.ProfilePanel;
import com.appspot.foodsorce.client.ui.FoodSorce;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyPressEvent;
import com.google.gwt.event.dom.client.KeyPressHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorListPanel extends VerticalPanel {

	private static final VendorListPanel INSTANCE = new VendorListPanel();
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	private FoodSorce foodSorce;

	private ScrollPanel scrollPanel;
	private VerticalPanel contentPanel = new VerticalPanel();
	private TextBox searchField = new TextBox();
	private FlowPanel searchPanel = new FlowPanel();
	private Button searchButton = new Button("Search");
	private FlexTable vendorTable = new FlexTable();

	private String searchText = "";
	private ArrayList<Vendor> allVendors = new ArrayList<Vendor>();
	private ArrayList<Vendor> nearbyVendors = new ArrayList<Vendor>();
	private ArrayList<Vendor> matchingVendors = new ArrayList<Vendor>();
	private Vendor selectedVendor;

	private VendorListPanel() {
		GWT.log("VendorListPanel.java: VendorListPanel() constructor");
		createLayout();
		fetchAllVendors();
	}

	private void createLayout() {
		// Search field settings
		searchField.setText("Seafood, hot dogs, burgers...");
		searchField.setWidth("580px");
		searchField.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchField.selectAll();
				searchField.setFocus(true);
			}
		});
		searchField.addKeyPressHandler(new KeyPressHandler() {
			public void onKeyPress(KeyPressEvent event) {
				boolean enterPressed = KeyCodes.KEY_ENTER == event.getNativeEvent().getKeyCode();
				if (enterPressed) {
					searchVendor();
				}
			}
		});
		searchField.setFocus(true);

		// Search button settings
		searchButton.addStyleName("searchButton");
		searchButton.setWidth("100px");
		searchButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				searchVendor();
			}
		});
		
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

		// Assemble panels
		searchPanel.add(searchField);
		searchPanel.add(searchButton);
		contentPanel.add(searchPanel);
		contentPanel.add(vendorTable);
		scrollPanel = new ScrollPanel(contentPanel);
		scrollPanel.setHeight("655px");
		this.add(scrollPanel);
	}

	public static VendorListPanel getInstance() {
		return INSTANCE;
	}

	public void setFoodSorce(FoodSorce foodSorce) {
		this.foodSorce = foodSorce;
	}

	private void fetchAllVendors() {
		vendorService.getVendors(new AsyncCallback<Vendor[]>() {
			public void onFailure(Throwable error) {
				GWT.log("VendorListPanel.java: fetchAllVendors onFailure", error);
				handleError(error);
			}

			public void onSuccess(Vendor[] result) {
				GWT.log("VendorListPanel.java: fetchAllVendors onSuccess");
				allVendors = new ArrayList<Vendor>(Arrays.asList(result));
				setupMapSearchPanel();
			}
		});
	}

	private void setupMapSearchPanel() {
		mapSearchPanel = MapSearchPanel.getInstance();
		mapSearchPanel.setAllVendors(allVendors);
		mapSearchPanel.setNearbyVendors(allVendors);
		mapSearchPanel.plotNearbyVendors();
		
		setNearbyVendors(allVendors);
		displayNearbyVendors();
	}

	public void searchVendor() {
		setSearchText(searchField.getText().toLowerCase());
		
		mapSearchPanel = MapSearchPanel.getInstance();
		mapSearchPanel.setNearbyVendors(allVendors);
		mapSearchPanel.filterAllVendorsIntoNearbyVendors();
		
		setNearbyVendors(mapSearchPanel.getNearbyVendors());
		filterNearbyVendorsIntoMatchingVendors();
		setNearbyVendors(matchingVendors);
		displayNearbyVendors();
		
		mapSearchPanel.setNearbyVendors(matchingVendors);
		mapSearchPanel.plotNearbyVendors();
	}
	
	public void filterNearbyVendorsIntoMatchingVendors() {
		if (searchText.equals("")) {
			matchingVendors = new ArrayList<Vendor>(nearbyVendors);
			GWT.log("VendorListPanel.java: filterNearbyVendors(): matchingVendors = " + matchingVendors.toString());
			return;
		}
	
		ArrayList<Vendor> vendorsToKeep = new ArrayList<Vendor>();
		for (Vendor vendor : nearbyVendors) {
			String name = null, description = null, location = null;
			try {
				name = vendor.getName().toLowerCase();
				description = vendor.getDescription().toLowerCase();
				location = vendor.getLocation().toLowerCase();
			} catch (Throwable e) {
				// Do nothing
			}
			if (name != null && name.contains(searchText)) {
				vendorsToKeep.add(vendor);
			} else if (description != null && description.contains(searchText)) {
				vendorsToKeep.add(vendor);
			} else if (location != null && location.contains(searchText)) {
				vendorsToKeep.add(vendor);
			}
		}
		matchingVendors = new ArrayList<Vendor>(vendorsToKeep);
		GWT.log("VendorListPanel.java: filterNearbyVendors(): matchingVendors = " + matchingVendors.toString());
	}
	
	public void setNearbyVendors(ArrayList<Vendor> nearbyVendors) {
		this.nearbyVendors = new ArrayList<Vendor>(nearbyVendors);
	}
	
	public void displayNearbyVendors() {
		displayVendors(nearbyVendors);
	}

	private void displayVendors(List<Vendor> vendors) {
		// Remove all rows except first header row
		int numRows = vendorTable.getRowCount();
		for (int i = 1; i < numRows; i++) {
			// Remove the second row, numRow times
			vendorTable.removeRow(1);
		}

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
		MapSearchPanel.getInstance().plotSelectedVendor(selectedVendor);
	}

	private void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			// Do nothing. Instantiating and viewing the VendorListPanel
			// without being logged in is allowed.
		} else {
			GWT.log("VendorListPanel.java: handleError", error);
			Window.alert(error.getMessage());
		}
	}

	public void setSearchText(String searchText) {
		this.searchText = searchText;
		searchField.setText(searchText);
		ProfilePanel.getInstance().setSearchText(searchText);
	}
	
	public ArrayList<Vendor> getMatchingVendors() {
		return matchingVendors;
	}

}