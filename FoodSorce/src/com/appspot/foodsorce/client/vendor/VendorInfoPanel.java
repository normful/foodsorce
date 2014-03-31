package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.LoginInfo;
import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.client.profile.ProfileServiceAsync;
import com.appspot.foodsorce.shared.Vendor;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
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
	private Button favouriteButton;
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

		checkIfFavourited(vendor, loginInfo);

		if (loginInfo != null) {
			createFavouritesButton(vendor, loginInfo);
			htmlPanel.add(favouriteButton);
		}

		scrollPanel.add(htmlPanel);
		this.add(scrollPanel);
	}

	private void createFavouritesButton (final Vendor vendor, final LoginInfo loginInfo) {
		if (favourited == false) {
			favouriteButton = new Button("Add to Favourite");
			favouriteButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					vendor.addFavourites(loginInfo.getEmailAddress());
					vendorService.setVendor(vendor, new AsyncCallback<Void>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub

						}
					});}
			});
		} else {
			favouriteButton = new Button("Delete from Favourite");
			favouriteButton.addClickHandler(new ClickHandler(){

				@Override
				public void onClick(ClickEvent event) {
					vendor.removeFavourites(loginInfo.getEmailAddress());
					vendorService.setVendor(vendor, new AsyncCallback<Void>(){

						@Override
						public void onFailure(Throwable caught) {
							// TODO Auto-generated method stub

						}

						@Override
						public void onSuccess(Void result) {
							// TODO Auto-generated method stub

						}
					});
				}
			});
		}
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
