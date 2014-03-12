package com.appspot.foodsorce.client.ui;

import com.appspot.foodsorce.client.admin.AdminPanel;
import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.client.login.LoginPanel;
import com.appspot.foodsorce.client.login.LoginService;
import com.appspot.foodsorce.client.login.LoginServiceAsync;
import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.map.MapSearchPanel;
import com.appspot.foodsorce.client.profile.ViewProfilePanel;
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

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class FoodSorce implements EntryPoint {
	
	private LoginInfo loginInfo = null;
	
	// Main panels
	private RootLayoutPanel root = RootLayoutPanel.get();
	private DockLayoutPanel dock = new DockLayoutPanel(Unit.EM);
	
	// Center panels
	private SimpleLayoutPanel center = new SimpleLayoutPanel();
	private LoginPanel loginPanel = new LoginPanel();
	private AdminPanel adminPanel = new AdminPanel();
	private ViewProfilePanel viewProfilePanel;
	// TODO for Norman
	// private EditProfilePanel editProfilePanel = new EditProfilePanel();
	private VendorListPanel vendorListPanel = VendorListPanel.getInstance();
	
	// North panel
	private FlowPanel north = new FlowPanel();
	
	// East panel
	private FlowPanel east = new FlowPanel();
	private MapSearchPanel mapSearchPanel = MapSearchPanel.getInstance();
	
	// West panels
	private FlowPanel west = new FlowPanel();
	private NavigationPanel navigationPanel = new NavigationPanel(this);

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
		
		createLayout();
		
		LoginServiceAsync loginService = GWT.create(LoginService.class);
		loginService.login(GWT.getHostPageBaseURL(), new AsyncCallback<LoginInfo>() {
			public void onFailure(Throwable error) {
				handleError(error);
		}
			public void onSuccess(LoginInfo result) {
				loginInfo = result;
						System.out
								.println("FoodSorce.java: onModuleLoad: loginService.login RPC success. loginInfo="
										+ loginInfo.toString());
						if (loginInfo.isLoggedIn()) {
							loadVendorListPanel();
							viewProfilePanel = new ViewProfilePanel(loginInfo
									.getEmailAddress());
						} else
							loadLoginPanel();
			}
		});
	}
	
	private void createLayout() {
		System.out.println("FoodSorce.java: createLayout()");
		
		HTML header = new HTML("<h1>FoodSorce</h1>");
		north.add(header);
		
		east.add(mapSearchPanel);
		
		west.add(navigationPanel);
	
		dock.addNorth(north, 8);
		dock.addEast(east, 40);
		dock.addWest(west, 10);
		dock.add(center);
		
		root.add(dock);
	}
	
	public void handleError(Throwable error) {
		if (error instanceof NotLoggedInException) {
			System.out.println("FoodSorce.java: NotLoggedInException thrown");
			loadLoginPanel();
		} else {
			Window.alert(error.getMessage());
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
			navigationPanel.setSignOutLink(loginInfo.getLogoutUrl());
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

	public void loadEditProfilePanel() {
		System.out.println("FoodSorce.java loadEditProfilePanel()");
		// TODO for Norman
//		center.setWidget(editProfilePanel);
	}

	public LoginInfo getLoginInfo() {
		return loginInfo;
	}

}