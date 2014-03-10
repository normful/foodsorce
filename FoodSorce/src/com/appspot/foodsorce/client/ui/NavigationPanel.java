package com.appspot.foodsorce.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NavigationPanel extends VerticalPanel {
	
	private FoodSorce foodSorce;
	
	private Anchor mainPageLink = new Anchor("Main Page");
	private Anchor viewProfileLink = new Anchor("Profile");
	private Anchor adminLink = new Anchor("Admin");
	private Anchor signOutLink = new Anchor("Sign Out");
	
	public NavigationPanel(FoodSorce foodSorce) {
		this.foodSorce = foodSorce;
		createLinks();
	}
	
	private void createLinks() {
		mainPageLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				foodSorce.loadVendorListPanel();
			}
		});
		mainPageLink.addStyleName("navigationLink");
		add(mainPageLink);
		
		viewProfileLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				foodSorce.loadViewProfilePanel();
			}
		});
		viewProfileLink.addStyleName("navigationLink");
		add(viewProfileLink);
		
		adminLink.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				foodSorce.loadAdminPanel();
			}
		});
		adminLink.addStyleName("navigationLink");
		add(adminLink);
		
		signOutLink.addStyleName("navigationLink");
		setSignOutLink(GWT.getHostPageBaseURL());
		add(signOutLink);
	}
	
	public void setSignOutLink(String url) {
		signOutLink.setHref(url);
	}

}