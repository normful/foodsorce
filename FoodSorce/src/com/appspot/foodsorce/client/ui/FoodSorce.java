package com.appspot.foodsorce.client.ui;

import com.appspot.foodsorce.client.admin.AdminPanel;
import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.client.login.LoginPanel;
import com.appspot.foodsorce.client.login.LoginService;
import com.appspot.foodsorce.client.login.LoginServiceAsync;
import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.profile.ProfilePanel;
import com.appspot.foodsorce.client.vendor.VendorInfoPanel;
import com.appspot.foodsorce.client.vendor.VendorListPanel;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.GWT.UncaughtExceptionHandler;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.RootLayoutPanel;
import com.google.gwt.user.client.ui.SimpleLayoutPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class FoodSorce implements EntryPoint {
	
	private LoginServiceAsync loginService = GWT.create(LoginService.class);
	private LoginInfo loginInfo = null;
	
	// Main panels
	private RootLayoutPanel root = RootLayoutPanel.get();
	private DockLayoutPanel dock = new DockLayoutPanel(Unit.EM);
	
	// Center panels
	private SimpleLayoutPanel center = new SimpleLayoutPanel();
	private LoginPanel loginPanel = new LoginPanel();
	private AdminPanel adminPanel;
	private ProfilePanel viewProfilePanel = ProfilePanel.getInstance();
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();
	// TODO: Uncomment the following line after ViewReviewsPanel is implemented
	// (Do not instantiate a viewReviewsPanel here; wait until the TODO further down
	// because ViewReviewsPanel should take the userEmail as a parameter in its constructor)
	// private ViewReviewsPanel viewReviewsPanel;
	private VendorInfoPanel vendorInfoPanel;
	
	// North panel
	private HorizontalPanel north = new HorizontalPanel();
	private VerticalPanel spacerPanel = new VerticalPanel();
	private TitlePanel headerPanel = new TitlePanel();
	private SocialMediaPanel socialMediaPanel = new SocialMediaPanel();
	
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
		vendorListPanel.setFoodSorce(this);
		mapSearchPanel.setFoodSorce(this);
		
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			@Override
			public void onSuccess(LoginInfo result) {
				GWT.log("FoodSorce.java: onModuleLoad: loginService.login RPC success. loginInfo=" + result.toString());
				loginInfo = result;
				if (result.isLoggedIn()) {
					loadNavigationPanel("loggedIn");
					loadVendorListPanel();
					viewProfilePanel.setUserEmail(result.getEmailAddress());
					viewProfilePanel.getProfile();
					// TODO: Uncomment the following line after RatingPanel is implemented
					// viewReviewsPanel = new ViewReviewsPanel(loginInfo.getEmailAddress());
				} else {
					loadNavigationPanel("loggedOut");
					loadVendorListPanel();
				}
			}
			
			@Override
			public void onFailure(Throwable error) {
				GWT.log("FoodSorce.java: onModuleLoad: loginService.login RPC failure" + error);
				handleError(error);
			}
		});
	}
	
	private void loadNavigationPanel(String loginState) {
		GWT.log("FoodSorce.java loadNavigationPanel(" + loginState + ")");
		if (loginState != null && loginState.equals("loggedIn"))
			navigationPanel = new NavigationPanel(this, true);
		else
			navigationPanel = new NavigationPanel(this, false);
		west.setWidget(navigationPanel);
	}
	
	private void createLayout() {
		GWT.log("FoodSorce.java: createLayout()");
		spacerPanel.setStyleName("north");
		north.add(spacerPanel);
		north.add(headerPanel);
		north.add(socialMediaPanel);
		north.setCellWidth(spacerPanel, "25%");
		north.setCellWidth(headerPanel, "50%");
		north.setCellWidth(socialMediaPanel, "25%");
		north.setWidth("100%");
		north.setCellHorizontalAlignment(headerPanel, HasHorizontalAlignment.ALIGN_CENTER);
		north.setCellVerticalAlignment(socialMediaPanel, HasVerticalAlignment.ALIGN_BOTTOM);
		north.setCellHorizontalAlignment(socialMediaPanel, HasHorizontalAlignment.ALIGN_RIGHT);
		north.setStyleName("north");
		
		east.add(mapSearchPanel);
		
		dock.addNorth(north, 8);
		dock.addEast(east, 40);
		dock.addWest(west, 14);
		dock.add(center);
		root.add(dock);
	}
	
	public void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			GWT.log("FoodSorce.java: NotLoggedInException thrown");
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
		GWT.log("FoodSorce.java: loadLoginPanel()");
		loginPanel.setSignInLink((loginInfo.getLoginUrl()));
		center.setWidget(loginPanel);
	}
	
	public void loadVendorListPanel() {
		GWT.log("FoodSorce.java: loadVendorListPanel()");
		if (loginInfo != null)
			navigationPanel.setLogoutLink(loginInfo.getLogoutUrl());
		center.setWidget(vendorListPanel);
		vendorListPanel.setNearbyVendors(mapSearchPanel.getNearbyVendors());
		vendorListPanel.displayNearbyVendors();
		mapSearchPanel.plotNearbyVendors();
	}
	
	public void loadVendorInfoPanel(Vendor vendor) {
		GWT.log("FoodSorce.java: loadVendorInfoPanel(" + vendor.getName() + ")");
		
		vendorInfoPanel = new VendorInfoPanel(vendor,loginInfo.getEmailAddress());
		center.setWidget(vendorInfoPanel);
	}

	public void loadAdminPanel() {
		GWT.log("FoodSorce.java loadAdminPanel()");
		adminPanel = AdminPanel.getInstance();
		center.setWidget(adminPanel);
	}
	
	public void loadViewProfilePanel() {
		GWT.log("FoodSorce.java loadViewProfilePanel()");
		center.setWidget(viewProfilePanel);
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}
	
}