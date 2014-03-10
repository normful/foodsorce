package com.appspot.foodsorce.client.login;

import com.google.gwt.user.client.ui.Anchor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class LoginPanel extends VerticalPanel {
	
	private	HTMLPanel htmlPanel = new HTMLPanel("<h2>Log In</h2><br>"
				+ "Please log in with your Google Account.");
	private Anchor loginLink = new Anchor("Log In");
		
	public LoginPanel() {
		add(htmlPanel);
		add(loginLink);
	}
	
	public void setSignInLink(String url) {
		loginLink.setHref(url);
	}
}