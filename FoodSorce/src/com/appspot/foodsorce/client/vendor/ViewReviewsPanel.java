package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewReviewsPanel extends VerticalPanel {

	Vendor vendor;

	public ViewReviewsPanel(Vendor vendor) {
		this.vendor = vendor;
		ArrayList<Review> reviews = vendor.getReviews();
		if (!vendor.getReviews().isEmpty())
			createReviewsTable(reviews);
	}

	private void createReviewsTable(ArrayList<Review> reviews) {
		FlexTable reviewsTable = new FlexTable();
		reviewsTable.setText(0, 0, "Reviewer");
		reviewsTable.setText(0, 1, "Quality");
		reviewsTable.setText(0, 2, "Cost");
		reviewsTable.setText(0, 3, "Comments");
		for (Review r : reviews) {
			int i = reviewsTable.getRowCount();
			reviewsTable.setText(i, 0, r.getUserEmail());
			reviewsTable.setWidget(i, 1, VendorListPanel.getInstance().vendorStarsImage(r.getQuality()));
			reviewsTable.setHTML(i, 2, renderCost(r.getCost()));
			reviewsTable.getCellFormatter().addStyleName(i, 2, "vendorCostDollarSignsText");
			reviewsTable.setText(i, 3, r.getText());
		}
		this.add(reviewsTable);
	}

	private String renderCost(int cost) {
		String costs = "";
		for (int i = 0; i < cost; i++)
			costs = costs + "$";
		return costs;
	}

}
