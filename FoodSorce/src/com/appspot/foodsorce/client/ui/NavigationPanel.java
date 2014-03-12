package com.appspot.foodsorce.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NavigationPanel extends VerticalPanel {

	private FoodSorce foodSorce;
	private boolean loggedIn;

	private Anchor mainPageLink = new Anchor("Find Food");
	private Anchor viewProfileLink = new Anchor("Profile");
	private Anchor adminLink = new Anchor("Admin");
	private Anchor loginLink = new Anchor("Log In");
	private Anchor logoutLink = new Anchor("Log Out");

	public NavigationPanel(FoodSorce foodSorce, boolean loggedIn) {
		this.foodSorce = foodSorce;
		this.loggedIn = loggedIn;
		createLinks();
	}

	private void createLinks() {
		mainPageLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				try {
					foodSorce.loadVendorListPanel();
				} catch (Throwable e) {
					e.printStackTrace();
				}
			}
		});
		mainPageLink.addStyleName("navigationLink");
		add(mainPageLink);

		if (loggedIn && foodSorce.getLoginInfo().isAdmin()) {
			adminLink.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					foodSorce.loadAdminPanel();
				}
			});
			adminLink.addStyleName("navigationLink");
			add(adminLink);
		}

		if (loggedIn) {
			viewProfileLink.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					foodSorce.loadViewProfilePanel();
				}
			});
			viewProfileLink.addStyleName("navigationLink");
			add(viewProfileLink);

			logoutLink.addStyleName("navigationLink");
			setLogoutLink(GWT.getHostPageBaseURL());
			add(logoutLink);
		} else {
			loginLink.addClickHandler(new ClickHandler() {
				public void onClick(ClickEvent event) {
					try {
						foodSorce.loadLoginPanel();
					} catch (Throwable e) {
						// Do nothing. This occurs when login link is shown but user is actually already logged in
					}
				}
			});
			loginLink.addStyleName("navigationLink");
			add(loginLink);
		}
	}

	public void setLogoutLink(String url) {
		if (url != null && !url.isEmpty())
			logoutLink.setHref(url);
		else
			logoutLink.setHref(GWT.getHostPageBaseURL());
	}

}