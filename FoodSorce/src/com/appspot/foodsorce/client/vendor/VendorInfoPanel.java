package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.LoginInfo;
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

	private boolean favourited;
	private Vendor currentVendor;

	public VendorInfoPanel(Vendor vendor, LoginInfo loginInfo) {
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
		htmlPanel.add(new HTML("<br>"));
		htmlPanel.add(addReviewButton);
		htmlPanel.add(viewReviewsPanel);

		setFavouriteButtons(vendor, loginInfo);

		scrollPanel.add(htmlPanel);
		this.add(scrollPanel);
	}

	private void setFavouriteButtons(Vendor vendor, LoginInfo loginInfo) {
		if (loginInfo.isLoggedIn()) {
			currentVendor = vendor;
			checkIfFavourited(loginInfo);
			setButtonToAdd(loginInfo);
			setButtonToRemove(loginInfo);
			htmlPanel.add(addFavouriteButton);
			htmlPanel.add(removeFavouriteButton);
		}

	}

	private void setButtonToAdd(final LoginInfo loginInfo) {
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
					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToAdd() onSuccess");
						favourited = true;
						System.out.println(currentVendor.getFavourites());
						ProfilePanel.getInstance().setFavouriteVendors();
					}
				});}
		});
	}

	private void setButtonToRemove (final LoginInfo loginInfo) {
		removeFavouriteButton = new Button();
		removeFavouriteButton.setText("Delete from Favourite");
		removeFavouriteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				System.out.println("begin onClick");
				if (favourited == false) {
					Window.alert("Not a favourited vendor");
					return;
				}
				
				currentVendor.removeFavourites(new UserEmail(loginInfo.getEmailAddress()));
				vendorService.setVendor(currentVendor, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onFailure");

					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onSuccess");
						favourited = false;
						System.out.println(currentVendor.getFavourites());
						ProfilePanel.getInstance().setFavouriteVendors();
					}
				});
			}
		});
	}

	private void checkIfFavourited(LoginInfo loginInfo) {
		for (UserEmail user : currentVendor.getFavourites()) {
			if (loginInfo.getEmailAddress().equals(user.getUserEmail())) {
				favourited = true;
			} else {
				favourited = false;
			}
		}
	}
}
