package com.appspot.foodsorce.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;

public class SocialMediaPanel extends HorizontalPanel {

	private HTML facebookButton;
	private HTML twitterButton;
	
	public SocialMediaPanel() {
		facebookButton = new HTML ("<div class=\"fb-like\" data-href=\"https://www.facebook.com/pages/Foodsorce/\" data-layout=\"standard\" data-action=\"like\" data-show-faces=\"false\" data-share=\"true\"></div>");
		twitterButton = new HTML("<a href=\"https://twitter.com/FoodSorce\" class=\"twitter-follow-button\" data-show-count=\"false\">Follow @FoodSorce</a>");
		add(facebookButton);
		add(twitterButton);
	}
}
