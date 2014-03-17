package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Vendor;
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
	
	public VendorInfoPanel(Vendor vendor) {
		vendorName = new Label(vendor.getName());
		vendorDescription = new Label(vendor.getDescription());
		vendorLocation = new Label(vendor.getLocation());
		vendorName.setStylePrimaryName("vendorInfoPanelName");
		vendorDescription.setStylePrimaryName("vendorInfoPanelDescription");
		vendorLocation.setStylePrimaryName("vendorInfoPanelLocation");
		htmlPanel.add(vendorName);
		htmlPanel.add(vendorDescription);
		htmlPanel.add(vendorLocation);
		scrollPanel.add(htmlPanel);
		this.add(scrollPanel);
	}
	
}
