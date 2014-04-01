package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.LoginInfo;
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

	private Vendor vendor;
	private String userEmail;
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("");
	private Label vendorName;
	private Label vendorLocation;
	private Label vendorDescription;
	private Button addReviewButton;
	private ViewReviewsPanel viewReviewsPanel;
	private AddReviewsPanel addReviewsPanel;

	public VendorInfoPanel(Vendor vendor, LoginInfo loginInfo) {
		this.vendor = vendor;
		this.userEmail = loginInfo.getEmailAddress();
		createHeader();
		if (loginInfo.isLoggedIn())
			createAddReviewsButton();
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
	}

	private void createAddReviewsButton() {
		addReviewButton = new Button("Add Review");
		addReviewsPanel = new AddReviewsPanel(vendor, userEmail, this);
		viewReviewsPanel = new ViewReviewsPanel(vendor);
		addReviewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove(viewReviewsPanel);
				add(addReviewsPanel);
				add(viewReviewsPanel);
			}
		});
		htmlPanel.add(new HTML("<br>"));
		htmlPanel.add(addReviewButton);
		htmlPanel.add(viewReviewsPanel);
	}

	private void createHeader() {
		vendorName = new Label(vendor.getName());
		vendorName.setStylePrimaryName("vendorInfoPanelName");
		vendorDescription = new Label(vendor.getDescription());
		vendorDescription.setStylePrimaryName("vendorInfoPanelDescription");
		vendorLocation = new Label(vendor.getLocation());
		vendorLocation.setStylePrimaryName("vendorInfoPanelLocation");
		htmlPanel.add(vendorName);
		htmlPanel.add(vendorDescription);
		htmlPanel.add(vendorLocation);
	}

	public void updateReviews() {
		Vendor vendor = viewReviewsPanel.getVendor();
		remove(viewReviewsPanel);
		viewReviewsPanel = new ViewReviewsPanel(vendor);
		add(viewReviewsPanel);
	}
}