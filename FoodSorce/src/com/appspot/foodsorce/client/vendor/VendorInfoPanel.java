package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.LoginInfo;
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
			checkIfFavourited(vendor, loginInfo);
			setButtonToAdd(vendor, loginInfo);
			setButtonToRemove(vendor, loginInfo);
			htmlPanel.add(addFavouriteButton);
			htmlPanel.add(removeFavouriteButton);
		}

	}

	private void setButtonToAdd(final Vendor vendor, final LoginInfo loginInfo) {
		System.out.println("in SetButtonAdd");
		addFavouriteButton = new Button();
		addFavouriteButton.setText("Add to Favourite");
		System.out.println("before handler");
		addFavouriteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (favourited == true) {
					Window.alert("Already a favourited vendor");
					return;
				}
				vendor.addFavourites(loginInfo.getEmailAddress());
				vendorService.setVendor(vendor, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("VendorInfoPanel.java: setButtonToAdd() onFailure");
					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToAdd() onSuccess");
						favourited = true;
					}
				});}
		});
	}

	private void setButtonToRemove (final Vendor vendor, final LoginInfo loginInfo) {
		removeFavouriteButton = new Button();
		System.out.println("in SetButtonRemove");
		removeFavouriteButton.setText("Delete from Favourite");
		removeFavouriteButton.addClickHandler(new ClickHandler(){

			@Override
			public void onClick(ClickEvent event) {
				if (favourited == false) {
					Window.alert("Not a favourited vendor");
					return;
				}
				vendor.removeFavourites(loginInfo.getEmailAddress());
				vendorService.setVendor(vendor, new AsyncCallback<Void>(){

					@Override
					public void onFailure(Throwable caught) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onFailure");

					}

					@Override
					public void onSuccess(Void result) {
						GWT.log("VendorInfoPanel.java: setButtonToRemove() onSuccess");
						favourited = false;
					}
				});
			}
		});
	}

	private void checkIfFavourited(Vendor vendor, LoginInfo loginInfo) {
		for (String user : vendor.getFavourites()) {
			if (loginInfo.getEmailAddress() == user) {
				favourited = true;
			} else {
				favourited = false;
			}
		}
	}
}
