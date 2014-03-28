package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddReviewsPanel extends VerticalPanel{
	private Vendor vendor;
	private int cost;
	private int quality;
	private String review;

	public AddReviewsPanel(Vendor vendor) {
		this.vendor=vendor;
	}

	public void constructWidgets() {
		RadioButton q1 = new RadioButton("quality","1 ");
		RadioButton q15 = new RadioButton("quality","1.5 ");
		RadioButton q2 = new RadioButton("quality","2 ");
		RadioButton q25 = new RadioButton("quality","2.5 ");
		RadioButton q3 = new RadioButton("quality","3 ");
		RadioButton q35 = new RadioButton("quality","3.5 ");
		RadioButton q4 = new RadioButton("quality","4 ");
		RadioButton q45 = new RadioButton("quality","4.5 ");
		RadioButton q5 = new RadioButton("quality","5 ");
		q5.setValue(true);
		this.add(new HTML("Vendor quality out of 5"));
		
		FlowPanel qualityPanel=new FlowPanel();
		qualityPanel.add(q1);
		qualityPanel.add(q15);
		qualityPanel.add(q2);
		qualityPanel.add(q25);
		qualityPanel.add(q3);
		qualityPanel.add(q35);
		qualityPanel.add(q4);
		qualityPanel.add(q45);
		qualityPanel.add(q5);
		this.add(qualityPanel);
		
		this.add(new HTML("Vendor cost"));
		RadioButton c1 = new RadioButton("cost","$ "); 
		RadioButton c2 = new RadioButton("cost","$$ "); 
		RadioButton c3 = new RadioButton("cost","$$$ "); 
		RadioButton c4 = new RadioButton("cost","$$$$ "); 
		RadioButton c5 = new RadioButton("cost","$$$$$ ");
		c1.setValue(true);
		
		FlowPanel costPanel = new FlowPanel();
		
		costPanel.add(c1);
		costPanel.add(c2);
		costPanel.add(c3);
		costPanel.add(c4);
		costPanel.add(c5);
		this.add(costPanel);
		
		this.add(new HTML("Please describe your experience with this vendor."));
		
		TextArea reviewArea = new TextArea();
		this.add(reviewArea);
		
		Button submitButton = new Button("Submit review");
		this.add(submitButton);
		


	}



}
