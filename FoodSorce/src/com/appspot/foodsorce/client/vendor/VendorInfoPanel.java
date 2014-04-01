package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.shared.Review;
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
	private Boolean isLoggedIn;
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
		this.isLoggedIn = loginInfo.isLoggedIn();
		createHeader();
		createViewReviewsPanel();
		htmlPanel.add(viewReviewsPanel);
		if (!hasReviewedVendor()) {
			createAddReviewsButton();
			htmlPanel.add(addReviewButton);
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
		htmlPanel.add(new HTML("<br><br>"));
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
				if (isLoggedIn) {
					htmlPanel.add(addReviewsPanel);
					removeAddReviewButton();
				} else {
					VendorListPanel.getInstance().getFoodSorce().loadLoginPanel();
				}
			}
		});
	}
	
	void createViewReviewsPanel() {
		viewReviewsPanel = new ViewReviewsPanel(vendor);
		htmlPanel.add(viewReviewsPanel);
	}

	public void removeViewReviewsPanel() {
		htmlPanel.remove(viewReviewsPanel);
	}
	
	public void removeAddReviewButton() {
		htmlPanel.remove(addReviewButton);
	}
	
	public void removeAddReviewsPanel() {
		htmlPanel.remove(addReviewsPanel);
	}
}