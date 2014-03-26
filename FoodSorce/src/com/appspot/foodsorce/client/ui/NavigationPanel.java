package com.appspot.foodsorce.client.ui;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class NavigationPanel extends VerticalPanel {

	private FoodSorce foodSorce;
	private boolean loggedIn;

	private Anchor mainPageLink = new Anchor("Find Food");
	private Anchor viewProfileLink = new Anchor("Profile");
	private Anchor adminLink = new Anchor("Admin");
	private Anchor loginLink = new Anchor("Log In");
	private Anchor logoutLink = new Anchor("Log Out");
	private HorizontalPanel socialMediaPanel;

	public NavigationPanel(FoodSorce foodSorce, boolean loggedIn) {
		this.foodSorce = foodSorce;
		this.loggedIn = loggedIn;
		createLinks();
		createSocialMediaPanel();

	}

	private void createSocialMediaPanel() {
		HTML twitterButton = new HTML("<a href=\"https://twitter.com/FoodSorce\" class=\"twitter-follow-button\" data-show-count=\"false\">Follow @FoodSorce</a><script>!function(d,s,id){var js,fjs=d.getElementsByTagName(s)[0],p=/^http:/.test(d.location)?'http':'https';if(!d.getElementById(id)){js=d.createElement(s);js.id=id;js.src=p+'://platform.twitter.com/widgets.js';fjs.parentNode.insertBefore(js,fjs);}}(document, 'script', 'twitter-wjs');</script>");
		VerticalPanel socialMediaPanel = new VerticalPanel();

		
		HTML facebookButton = new HTML ("<div class=\"fb-like\" data-href=\"https://www.facebook.com/pages/Foodsorce/\" data-layout=\"standard\" data-action=\"like\" data-show-faces=\"false\" data-share=\"true\"></div>");
		socialMediaPanel.add(facebookButton);
		socialMediaPanel.add(twitterButton);
		this.add(socialMediaPanel);
		
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