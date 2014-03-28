package com.appspot.foodsorce.client.vendor;

import java.util.ArrayList;

import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ViewReviewsPanel extends VerticalPanel {

	Vendor vendor;
	
	public ViewReviewsPanel(Vendor vendor) {
		this.vendor = vendor;
		ArrayList<Review> reviews = vendor.getReviews();
		
		FlexTable reviewsTable = new FlexTable();
		reviewsTable.setText(0, 0, "Reviewer");
		reviewsTable.setText(0, 1, "Quality");
		reviewsTable.setText(0, 2, "Cost");
		reviewsTable.setText(0, 3, "Comments");
		for(Review r:reviews){
			int i=reviewsTable.getRowCount();
			reviewsTable.setText(i,0,r.getUserEmail());
			reviewsTable.setHTML(i, 1, renderQuality(r.getQuality()));
			reviewsTable.setHTML(i, 2, renderCost(r.getCost()));
			reviewsTable.setText(i, 3, r.getText());
		}
	}

	private String renderCost(int cost) {
		// TODO Auto-generated method stub
		return null;
	}

	private String renderQuality(int quality) {
		// TODO Auto-generated method stub
		return null;
	}
}
