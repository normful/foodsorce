package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.client.profile.ProfilePanel;
import com.appspot.foodsorce.shared.Vendor;
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
	private Boolean hasFavourited;
	private VendorServiceAsync vendorService = GWT.create(VendorService.class);
	private ScrollPanel scrollPanel = new ScrollPanel();
	private HTMLPanel htmlPanel = new HTMLPanel("");
	private Label vendorName;
	private Label vendorLocation;
	private Label vendorDescription;
	private Button addReviewButton = new Button("Add Review");
	private Button addFavouriteButton = new Button("Add to Favourites");
	private Button removeFavouriteButton = new Button("Remove from Favourites");
	private ViewReviewsPanel viewReviewsPanel;
	private AddReviewsPanel addReviewsPanel;
	private LoginInfo loginInfo;

	public VendorInfoPanel(Vendor vendor, LoginInfo loginInfo) {
		this.vendor = vendor;
		this.userEmail = loginInfo.getEmailAddress();
		this.isLoggedIn = loginInfo.isLoggedIn();
		this.loginInfo = loginInfo;
		createHeader();
		createViewReviewsPanel();
		htmlPanel.add(viewReviewsPanel);
		if (!hasReviewedVendor()) {
			createAddReviewsButton();
			htmlPanel.add(addReviewButton);
		}
		if (isLoggedIn)
			createFavouriteButtons();
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
			if (review.getUserEmail().equals(userEmail))
				return true;
		}
		return false;
	}

	private void createAddReviewsButton() {
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

	private void createFavouriteButtons() {
		htmlPanel.add(new HTML("<br>"));
		checkIfFavourited();
		if (hasFavourited) {
			createRemoveFavouriteButton();
			htmlPanel.add(removeFavouriteButton);
		} else {
			createAddFavouriteButton();
			htmlPanel.add(addFavouriteButton);
		}
	}

	private void createAddFavouriteButton() {
		addFavouriteButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (hasFavourited == true) {
					Window.alert("Already a favourited vendor");
					return;
				}
				vendor.addFavouriter(loginInfo.getEmailAddress());
				hasFavourited = true;
				htmlPanel.remove(addFavouriteButton);
				ProfilePanel.getInstance().setFavouriteVendors();
				updateVendor();
			}
		});
	}
	
	private void createRemoveFavouriteButton() {
		removeFavouriteButton.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (hasFavourited == false) {
					Window.alert("Not a favourited vendor");
					return;
				}
				vendor.removeFavouriter(loginInfo.getEmailAddress());
				hasFavourited = false;
				htmlPanel.remove(removeFavouriteButton);
				ProfilePanel.getInstance().setFavouriteVendors();
				updateVendor();
			}
		});
	}

	private void checkIfFavourited() {
		for (String favouriter : vendor.getFavouriters()) {
			if (loginInfo.getEmailAddress().equals(favouriter)) {
				hasFavourited = true;
				return;
			}
		}
		hasFavourited = false;
	}

	private void updateVendor() {
		vendorService = GWT.create(VendorService.class);
		vendorService.setVendor(vendor, new AsyncCallback<Void>(){
			public void onSuccess(Void result) {
			}
			public void onFailure(Throwable caught) {
				Window.alert("Failed to remove from favourites.");
			}
		});
	}
}