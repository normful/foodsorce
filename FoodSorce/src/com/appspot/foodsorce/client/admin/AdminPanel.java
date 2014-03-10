package com.appspot.foodsorce.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminPanel extends VerticalPanel {
	
	private Button importDataButton = new Button("Import Data");
	private VancouverDataServiceAsync dataService = GWT.create(VancouverDataService.class);
	
	public AdminPanel() {
		importDataButton.addStyleName("importDataButton");
		importDataButton.setWidth("100px");
		importDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dataService.importData(null);
			}
		});
		
		this.add(importDataButton);
	}
}
