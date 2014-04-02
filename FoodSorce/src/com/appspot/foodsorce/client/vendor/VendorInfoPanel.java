package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.client.profile.ProfilePanel;
import com.appspot.foodsorce.shared.UserEmail;
import com.appspot.foodsorce.shared.Vendor;
import com.google.appengine.api.datastore.Email;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
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
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("");
	private Label vendorName;
	private Label vendorLocation;
	private Label vendorDescription;
	private Button addReviewButton;
	private Button addFavouriteButton;
	private Button removeFavouriteButton;
	private ViewReviewsPanel viewReviewsPanel;
	private AddReviewsPanel addReviewsPanel;
	private boolean favourited;
	private Vendor currentVendor;
	private LoginInfo loginInfo;

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
		setFavouriteButtons(vendor, loginInfo);
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
		addReviewButton = new Button("Add Review");
		viewReviewsPanel = new ViewReviewsPanel(vendor);
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

	private void setFavouriteButtons(Vendor vendor, LoginInfo loginInfo) {
		if (loginInfo.isLoggedIn()) {
			this.loginInfo = loginInfo;
			currentVendor = vendor;
			checkIfFavourited();
			setButtonToAdd();
			setButtonToRemove();
			htmlPanel.add(addFavouriteButton);
			htmlPanel.add(removeFavouriteButton);
		}

	}

	private void setButtonToAdd() {
		addFavouriteButton = new Button();
		addFavouriteButton.setText("Add to Favourite");
		addFavouriteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (favourited == true) {
					Window.alert("Already a favourited vendor");
					return;
				}
				currentVendor.addFavourites(new UserEmail(loginInfo.getEmailAddress()));
				vendorService.setVendor(currentVendor, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("VendorInfoPanel.java: setButtonToAdd() onFailure");
						Window.alert("Failed to add to favourites.");
					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToAdd() onSuccess");
						favourited = true;
						ProfilePanel.getInstance().setFavouriteVendors();
					}
				});}
		});
	}

	private void setButtonToRemove () {
		removeFavouriteButton = new Button();
		removeFavouriteButton.setText("Delete from Favourite");
		removeFavouriteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (favourited == false) {
					Window.alert("Not a favourited vendor");
					return;
				}
				currentVendor.removeFavourites(new UserEmail(loginInfo.getEmailAddress()));
				vendorService.setVendor(currentVendor, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onFailure");
						Window.alert("Failed to remove from favourites.");

					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onSuccess");
						favourited = false;
						ProfilePanel.getInstance().setFavouriteVendors();
					}
				});
			}
		});
	}

	private void checkIfFavourited() {
		for (UserEmail user : currentVendor.getFavourites()) {
			if (loginInfo.getEmailAddress().equals(user.getUserEmail())) {
				favourited = true;
			} else {
				favourited = false;
			}
		}
	}
}
