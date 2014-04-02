package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewReviewsPanel extends VerticalPanel {

	Vendor vendor;

	public ViewReviewsPanel(Vendor vendor) {
		this.vendor = vendor;
		ArrayList<Review> reviews = vendor.getReviews();
		if (vendor == null || vendor.getReviews() == null)
			return;
		if (!vendor.getReviews().isEmpty())
			createReviewsTable(reviews);
	}

	private void createReviewsTable(ArrayList<Review> reviews) {
		add(new HTML("<h3>Reviews</h3><br>"));
		FlexTable reviewsTable = new FlexTable();
		reviewsTable.setCellPadding(5);
		reviewsTable.setText(0, 0, "Reviewer");
		reviewsTable.setText(0, 1, "Quality");
		reviewsTable.setText(0, 2, "Cost");
		reviewsTable.setText(0, 3, "Comments");
		reviewsTable.getColumnFormatter().setWidth(2, "50px");
		reviewsTable.getColumnFormatter().setWidth(3, "300px");
		reviewsTable.getRowFormatter().addStyleName(0, "viewReviewsPanelTableHeader");
		for (Review r : reviews) {
			int i = reviewsTable.getRowCount();
			reviewsTable.setText(i, 0, r.getUserEmail());
			reviewsTable.getCellFormatter().addStyleName(i, 0, "viewReviewsPanelUsername");
			reviewsTable.setWidget(i, 1, VendorListPanel.getInstance().vendorStarsImage(0.5*((double) r.getQuality())));
			reviewsTable.setHTML(i, 2, VendorListPanel.getInstance().vendorCostString(r.getCost()));
			reviewsTable.getCellFormatter().addStyleName(i, 2, "vendorCostDollarSignsText");
			reviewsTable.setText(i, 3, r.getText());
		}
		this.add(reviewsTable);
	}
}
