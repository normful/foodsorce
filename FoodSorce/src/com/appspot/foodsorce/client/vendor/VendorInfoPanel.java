package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorInfoPanel extends VerticalPanel {

	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("");
	private Label vendorName;
	private Label vendorLocation;
	private Label vendorDescription;
	private Button addReviewButton;
	private ViewReviewsPanel viewReviewsPanel;
	private AddReviewsPanel addReviewsPanel;
	
	public VendorInfoPanel(Vendor vendor,String userEmail) {
		vendorName = new Label(vendor.getName());
		vendorName.setStylePrimaryName("vendorInfoPanelName");
		
		vendorDescription = new Label(vendor.getDescription());
		vendorDescription.setStylePrimaryName("vendorInfoPanelDescription");
		
		vendorLocation = new Label(vendor.getLocation());
		vendorLocation.setStylePrimaryName("vendorInfoPanelLocation");
		
		addReviewButton = new Button("Add Review");
		addReviewsPanel= new AddReviewsPanel(vendor,userEmail);
		addReviewButton.addClickHandler(new ClickHandler() {



			@Override
			public void onClick(ClickEvent event) {

					remove(viewReviewsPanel);
					add(addReviewsPanel);
					add(viewReviewsPanel);
				}
			
		});
		viewReviewsPanel = new ViewReviewsPanel(vendor);
		
		htmlPanel.add(vendorName);
		htmlPanel.add(vendorDescription);
		htmlPanel.add(vendorLocation);
		htmlPanel.add(new HTML("<br>"));
		htmlPanel.add(addReviewButton);
		htmlPanel.add(viewReviewsPanel);
		
		scrollPanel.add(htmlPanel);
		this.add(scrollPanel);
	}
	
}
