package com.appspot.foodsorce.client.ui;

import java.util.ArrayList;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.client.login.LoginService;
import com.appspot.foodsorce.client.login.LoginServiceAsync;
import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.VendorInfo;
import com.appspot.foodsorce.client.vendor.VendorService;
import com.appspot.foodsorce.client.vendor.VendorServiceAsync;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodSorce implements EntryPoint {

	private RootLayoutPanel rlp = RootLayoutPanel.get();
	private DockLayoutPanel dlp = new DockLayoutPanel(Unit.EM);
	private SimpleLayoutPanel dlpCenter = new SimpleLayoutPanel();
	private SimpleLayoutPanel dlpWest = new SimpleLayoutPanel();

	// TODO: Split this class up to create new instances of helper classes
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("To use FoodSorce, please sign in with your Google Account.");
	private Anchor signInLink = new Anchor("Sign In");
	private Anchor signOutLink = new Anchor("Sign Out");
	private LoginInfo loginInfo = null;

	private VerticalPanel mainPanel = new VerticalPanel();
	private FlexTable vendorFlexTable = new FlexTable();
	
	private final VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private ArrayList<VendorInfo> vendors = new ArrayList<VendorInfo>();
	
//	private VendorRatingServiceAsync vendorRatingService = GWT.create(VendorRatingService.class);
	
	private static final int REFRESH_INTERVAL = 5000; // milliseconds
	
	/** Entry point method */
	public void onModuleLoad() {
		loadLayout();
		
		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					System.out.println("loadFoodSorce called");
					loadFoodSorce();
				} else {
					System.out.println("loadLogin called");
					loadLogin();
				}
			}
		});
	}

	private void loadLayout() {
		// North section
		dlp.addNorth(new HTML("<h1>FoodSorce</h1>"), 8);
		
		// East section
		dlp.addEast(new HTML("Google Maps plot to be inserted here"), 40);
	
		// West section
		Anchor mainPageLink = new Anchor("Main Page");
		mainPageLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadFoodSorce();
			}
		});
		
		Anchor viewProfileLink = new Anchor("Profile");
		viewProfileLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				loadViewProfile();
			}
		});
		
		VerticalPanel navigationPanel = new VerticalPanel();
		navigationPanel.add(mainPageLink);
		navigationPanel.add(viewProfileLink);
		dlpWest.add(navigationPanel);
		dlp.addWest(dlpWest, 20);
		
		// Center section
		dlp.add(dlpCenter);
		
		// Add dock layout panel to root layout panel
		rlp.add(dlp);
	}
	
	private void loadLogin() {
		// Assemble login panel
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		
		// Set loginPanel as center of dock layout panel
		dlpCenter.setWidget(loginPanel);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
	
	private void loadFoodSorce() {
		// Set up sign out URL
		signOutLink.setHref(loginInfo.getLogoutUrl());
		
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
		mainPanel.add(vendorFlexTable);
		mainPanel.add(signOutLink);
		
		// Set mainPanel as center of dock layout panel
		dlpCenter.setWidget(mainPanel);
		
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
	
	private void displayVendors(VendorInfo[] vendorInfos) {
		for (VendorInfo vendorInfo : vendorInfos) {
			displayVendor(vendorInfo);
		}
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
	
	private void loadViewProfile() {
		ScrollPanel scrollPanel = new ScrollPanel();
		HTMLPanel htmlPanel = new HTMLPanel("<h2>Profile Page</h2>");
		
// TODO: Fix this code to reflect refactoring
//		Image profilePhoto = new Image(loginInfo.getPhotoUrl(), 0, 0, 225, 225);
//		Label nicknameLabel = new Label("Nickname: " + loginInfo.getNickname());
//		Label emailLabel = new Label("Email: " + loginInfo.getEmailAddress());
//		Label headlineLabel = new Label("Headline: " + loginInfo.getHeadline());
//		Label genderLabel = new Label("Gender: " + loginInfo.getGender());
//		Label favouriteFoodLabel = new Label("Favourite Food: " + loginInfo.getFavouriteFood());
//		Label hometownLabel = new Label("Hometown: " + loginInfo.getHometown());
//		Label websiteUrlLabel = new Label("Website: " + loginInfo.getWebsiteUrl());
//		Anchor editProfileLink = new Anchor("Edit Profile");
//		editProfileLink.addClickHandler(new ClickHandler() {
//			public void onClick(ClickEvent event) {
//				loadEditProfile();
//			}
//		});
//		
//		htmlPanel.add(profilePhoto);
//		htmlPanel.add(nicknameLabel);
//		htmlPanel.add(emailLabel);
//		htmlPanel.add(headlineLabel);
//		htmlPanel.add(genderLabel);
//		htmlPanel.add(favouriteFoodLabel);
//		htmlPanel.add(hometownLabel);
//		htmlPanel.add(websiteUrlLabel);
//		htmlPanel.add(editProfileLink);
		
		scrollPanel.add(htmlPanel);
		dlpCenter.setWidget(scrollPanel);
	}
	
	private void loadEditProfile() {
		
	}
	
}