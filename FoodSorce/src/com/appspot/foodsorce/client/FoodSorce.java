package com.appspot.foodsorce.client;

//import java.util.ArrayList;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Anchor;
//import com.google.gwt.user.client.ui.Button;
//import com.google.gwt.user.client.ui.FlexTable;
//import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.RootPanel;
//import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodSorce implements EntryPoint {
	
//	private static final int REFRESH_INTERVAL = 5000; // milliseconds
//	private VerticalPanel mainPanel = new VerticalPanel(); 
//	private FlexTable stocksFlexTable = new FlexTable(); 
//	private HorizontalPanel addPanel = new HorizontalPanel(); 
//	private TextBox newSymbolTextBox = new TextBox(); 
//	private Button addStockButton = new Button("Add"); 
//	private Label lastUpdatedLabel = new Label();
//	private ArrayList<String> stocks = new ArrayList<String>();
//	
//	private Label errorMsgLabel = new Label();
	
	private LoginInfo loginInfo = null;
	private VerticalPanel loginPanel = new VerticalPanel();
	private Label loginLabel = new Label("Please sign in to your Google Account to access the FoodSorce application.");
	private Anchor signInLink = new Anchor("Sign In");
//	private Anchor signOutLink = new Anchor("Sign Out");
	
//	private final VendorServiceAsync vendorService = GWT.create(VendorService.class);

	/** * Entry point method. */ 
	public void onModuleLoad() {

		// Check login status using login service.
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
			}

			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				if(loginInfo.isLoggedIn()) {
					loadFoodSorce();
				} else {
					loadLogin();
				}
			}
		});
	}

	private void loadLogin() {
		// Assemble login panel.
		signInLink.setHref(loginInfo.getLoginUrl());
		loginPanel.add(loginLabel);
		loginPanel.add(signInLink);
		RootPanel.get("stockList").add(loginPanel);
	}

	private void handleError(Throwable error) {
		Window.alert(error.getMessage());
		if (error instanceof NotLoggedInException) {
			Window.Location.replace(loginInfo.getLogoutUrl());
		}
	}
	
	private void loadFoodSorce() {
	
	}
}
