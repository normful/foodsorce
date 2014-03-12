package com.appspot.foodsorce.client.admin;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AdminPanel extends VerticalPanel {
	
	private Button importDataButton = new Button("Import Data");
	private VancouverDataServiceAsync dataService;
	
	public AdminPanel() {
		HTMLPanel htmlPanel = new HTMLPanel("<h2>Administrator Console</h2><br>"
				+ "This button imports Vancouver food vendor data into FoodSorce.<br>");
		
		// TODO: Display a list of Profiles
		
		importDataButton.addStyleName("importDataButton");
		importDataButton.setWidth("100px");
		importDataButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				callImportData();
			}
		});
		
		this.add(htmlPanel);
		this.add(importDataButton);
	}
	
	private void callImportData() {
		dataService = GWT.create(VancouverDataService.class);
		dataService.importData(new AsyncCallback<Void>() {
			public void onFailure(Throwable error) {
				Window.alert("Failed to import data: " + error.toString());
			}
			public void onSuccess(Void voidResult) {
				Window.alert("Successfully imported data.");
			}
		});
	}
	
}
