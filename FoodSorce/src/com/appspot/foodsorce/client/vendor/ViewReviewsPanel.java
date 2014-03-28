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
		
		FlexTable reviewsTable = new FlexTable();
		reviewsTable.setText(0, 0, "Reviewer");
		reviewsTable.setText(0, 1, "Quality");
		reviewsTable.setText(0, 2, "Cost");
		reviewsTable.setText(0, 3, "Comments");
		for(Review r:reviews){
			int i=reviewsTable.getRowCount();
			reviewsTable.setText(i,0,r.getUserEmail());
			reviewsTable.setText(i, 1, renderQuality(r.getQuality()));
			reviewsTable.setText(i, 2, renderCost(r.getCost()));
			reviewsTable.setText(i, 3, r.getText());
		}
		this.add(reviewsTable);
	}

	private String renderCost(int cost) {
		String costs ="";
		for(int i=0;i<cost;i++){
			costs= costs + "$";
		}
		return costs;
	}

	private String renderQuality(int quality) {
		String stars ="";
		for(int i=0;i<quality;i++){
			if(i%2==0)
				stars=stars+"<";
			else
				stars=stars+">";
		}
		return stars;
	}
}
