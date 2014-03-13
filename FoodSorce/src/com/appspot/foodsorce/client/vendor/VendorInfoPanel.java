package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.ui.HTMLPanel;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;

public class VendorInfoPanel extends VerticalPanel {

	private Vendor vendor;
	
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel;
	
	public VendorInfoPanel(Vendor vendor) {
		this.vendor = vendor;
		htmlPanel = new HTMLPanel(vendor.getName());
		scrollPanel.add(htmlPanel);
		this.add(scrollPanel);
	}
	
}
