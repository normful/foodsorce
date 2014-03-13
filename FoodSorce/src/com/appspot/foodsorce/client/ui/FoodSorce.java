package com.appspot.foodsorce.client.ui;

import com.appspot.foodsorce.client.admin.AdminPanel;
import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.client.login.LoginPanel;
import com.appspot.foodsorce.client.login.LoginService;
import com.appspot.foodsorce.client.login.LoginServiceAsync;
import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.profile.ProfilePanel;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;

public class FoodSorce implements EntryPoint {
	
	private LoginInfo loginInfo = null;
	
	// Main panels
	private RootLayoutPanel root = RootLayoutPanel.get();
	private DockLayoutPanel dock = new DockLayoutPanel(Unit.EM);
	
	// Center panels
	private SimpleLayoutPanel center = new SimpleLayoutPanel();
	private LoginPanel loginPanel = new LoginPanel();
	private AdminPanel adminPanel = new AdminPanel();
	private ProfilePanel viewProfilePanel;
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();
	// TODO: Uncomment the following line after ViewReviewsPanel is implemented
	// (Do not instantiate a viewReviewsPanel here; wait until the TODO further down
	// because ViewReviewsPanel should take the userEmail as a parameter in its constructor)
	// private ViewReviewsPanel viewReviewsPanel;
	
	// North panel
	private FlowPanel north = new FlowPanel();
	
	// East panel
	private FlowPanel east = new FlowPanel();
	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	
	// West panels
	private SimpleLayoutPanel west = new SimpleLayoutPanel();
	private NavigationPanel navigationPanel;

	/** Entry point method **/
	public void onModuleLoad() {
		
		if(GWT.isProdMode()) {
			GWT.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
				@Override
				public void onUncaughtException(Throwable e) {
					Window.alert("An Uncaught Exception has occured : " + e.toString());
				}
			});
		}
		
		loadNavigationPanel("loggedOut");
		createLayout();
		west.setWidget(navigationPanel);
		center.setWidget(vendorListPanel);
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
				System.out.println("FoodSorce.java: onModuleLoad: loginService.login RPC success. loginInfo=" + loginInfo.toString());
				if (loginInfo.isLoggedIn()) {
					loadNavigationPanel("loggedIn");
					loadVendorListPanel();
					viewProfilePanel = new ProfilePanel(loginInfo.getEmailAddress());
					// TODO: Uncomment the following line after RatingPanel is implemented
					// viewReviewsPanel = new ViewReviewsPanel(loginInfo.getEmailAddress());
				} else {
					loadNavigationPanel("loggedOut");
					loadVendorListPanel();
				}
			}
			
			@Override
			public void onFailure(Throwable error) {
				handleError(error);
			}
		});
	}
	
	private void loadNavigationPanel(String loginState) {
		System.out.println("FoodSorce.java loadNavigationPanel(" + loginState + ")");
		if (loginState != null && loginState.equals("loggedIn"))
			navigationPanel = new NavigationPanel(this, true);
		else
			navigationPanel = new NavigationPanel(this, false);
		west.setWidget(navigationPanel);
	}
	
	private void createLayout() {
		System.out.println("FoodSorce.java: createLayout()");
		HTML header = new HTML("<h1>FoodSorce</h1>");
		
		north.add(header);
		east.add(mapSearchPanel);
		
		dock.addNorth(north, 8);
		dock.addEast(east, 40);
		dock.addWest(west, 14);
		dock.add(center);
		root.add(dock);
	}
	
	public void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			System.out.println("FoodSorce.java: NotLoggedInException thrown");
			loadLoginPanel();
		} else {
			if (loginInfo != null && !loginInfo.isLoggedIn())
				loadLoginPanel();
			else if (loginInfo != null && loginInfo.isLoggedIn())
				loadVendorListPanel();
			else
				Window.alert("handleError caught: " + error.getMessage());
		}
	}

	public void loadLoginPanel() {
		System.out.println("FoodSorce.java: loadLoginPanel()");
		loginPanel.setSignInLink((loginInfo.getLoginUrl()));
		center.setWidget(loginPanel);
	}
	
	public void loadVendorListPanel() {
		System.out.println("FoodSorce.java: loadVendorListPanel()");
		if (loginInfo != null)
			navigationPanel.setLogoutLink(loginInfo.getLogoutUrl());
		center.setWidget(vendorListPanel);
	}

	public void loadAdminPanel() {
		System.out.println("FoodSorce.java loadAdminPanel()");
		center.setWidget(adminPanel);
	}
	
	public void loadViewProfilePanel() {
		System.out.println("FoodSorce.java loadViewProfilePanel()");
		center.setWidget(viewProfilePanel);
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	
}