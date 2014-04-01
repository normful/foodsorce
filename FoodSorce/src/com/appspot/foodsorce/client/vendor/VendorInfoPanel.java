package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
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
		
		if (loginInfo.isLoggedIn() && !hasReviewedVendor()) {
			// Case 1: User is logged in and has not reviewed this Vendor
			// - create Add Reviews button which, when pressed, will create 
			//   an AddReviewsPanel and add it to this VendorInfoPanel
			// - create a ViewReviewsPanel
			createAddReviewsButton();
			createViewReviewsPanel();
			htmlPanel.add(addReviewButton);
		} else {
			// Case 2: User is logged out OR
			//         User is logged in and has already reviewed this Vendor
			// - do not create an Add Reviews button
			// - create a ViewReviewsPanel
			createViewReviewsPanel();
			htmlPanel.add(viewReviewsPanel);
		}
		scrollPanel.add(htmlPanel);
		add(scrollPanel);
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

	private boolean hasReviewedVendor() {
		ArrayList<Review> existingReviews = vendor.getReviews();
		if (existingReviews.isEmpty())
			return false;
		for (Review review : existingReviews) {
			if (review.getUserEmail().equals(userEmail)) {
				return true;
			}
		}
		return false;
	}

	private void createAddReviewsButton() {
		addReviewButton = new Button("Add Review");
		addReviewsPanel = new AddReviewsPanel(vendor, userEmail, this);
		addReviewButton.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				remove(addReviewButton);
				add(addReviewsPanel);
			}
		});
	}
	
	private void createViewReviewsPanel() {
		viewReviewsPanel = new ViewReviewsPanel(vendor);
		add(viewReviewsPanel);
	}

	public void removeAddReviewsPanel() {
		remove(addReviewsPanel);
	}
}