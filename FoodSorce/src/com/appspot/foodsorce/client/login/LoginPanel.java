package com.appspot.foodsorce.client.login;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends VerticalPanel {
	
	private Label loginLabel = new Label("Login with your Google Account to use FoodSorce.");
	private Anchor signInLink = new Anchor("Sign In");

	public LoginPanel() {
		add(loginLabel);
		add(signInLink);
	}
	
	public void setSignInLink(String url) {
		signInLink.setHref(url);
	}
}