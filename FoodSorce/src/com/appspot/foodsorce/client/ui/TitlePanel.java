package com.appspot.foodsorce.client.ui;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class TitlePanel extends VerticalPanel {

	HTML titleText = new HTML("<h1>FoodSorce</h1>"); 
	
	public TitlePanel() {
		add(titleText);
		addStyleName("titlePanel");
	}
}
