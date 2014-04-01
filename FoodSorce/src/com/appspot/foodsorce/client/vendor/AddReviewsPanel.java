package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.core.shared.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.RadioButton;
import com.google.gwt.user.client.ui.TextArea;
import com.google.gwt.user.client.ui.VerticalPanel;

public class AddReviewsPanel extends VerticalPanel {
	
	private VendorServiceAsync vendorService;
	private String userEmail;
	private Vendor vendor;
	private int cost = 1;
	private int quality = 10;
	private String reviewText = "";
	private TextArea reviewTextArea = new TextArea();
	private VendorInfoPanel vendorInfoPanel;

	public AddReviewsPanel(Vendor vendor, String userEmail,
			VendorInfoPanel toUpdate) {
		this.vendor = vendor;
		this.userEmail = userEmail;
		this.vendorInfoPanel = toUpdate;
		createUI();
	}

	public void createUI() {
		/*
		 * Header
		 */
		add(new HTML("<h3>Add Review</h3><br>"
				+ "Let us know what you thought about " + vendor.getName() + "!<br><br>"));
		
		/*
		 * Vendor quality
		 */
		FlowPanel qualityPanel = new FlowPanel();
		qualityPanel.add(new HTML("<h4><b>Quality</b></h4>"));
		
		RadioButton q1 = new RadioButton("quality", "1 ");
		q1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 2; }
		});
		RadioButton q15 = new RadioButton("quality", "1.5 ");
		q15.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 3; }
		});
		RadioButton q2 = new RadioButton("quality", "2 ");
		q2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 4; }
		});
		RadioButton q25 = new RadioButton("quality", "2.5 ");
		q25.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 5; }
		});
		RadioButton q3 = new RadioButton("quality", "3 ");
		q3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 6; }
		});
		RadioButton q35 = new RadioButton("quality", "3.5 ");
		q35.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 7; }
		});
		RadioButton q4 = new RadioButton("quality", "4 ");
		q4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 8; }
		});
		RadioButton q45 = new RadioButton("quality", "4.5 ");
		q45.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 9; }
		});
		RadioButton q5 = new RadioButton("quality", "5 ");
		q5.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { quality = 10; }
		});
		
		qualityPanel.add(q1);
		qualityPanel.add(q15);
		qualityPanel.add(q2);
		qualityPanel.add(q25);
		qualityPanel.add(q3);
		qualityPanel.add(q35);
		qualityPanel.add(q4);
		qualityPanel.add(q45);
		qualityPanel.add(q5);
		qualityPanel.add(new HTML("<br>"));
		this.add(qualityPanel);

		/*
		 * Vendor cost
		 */
		FlowPanel costPanel = new FlowPanel();
		costPanel.add(new HTML("<h4><b>Cost</b></h4>"));
		
		RadioButton c1 = new RadioButton("cost", "$ ");
		c1.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { cost = 1; }
		});
		RadioButton c2 = new RadioButton("cost", "$$ ");
		c2.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { cost = 2; }
		});
		RadioButton c3 = new RadioButton("cost", "$$$ ");
		c3.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { cost = 3; }
		});
		RadioButton c4 = new RadioButton("cost", "$$$$ ");
		c4.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { cost = 4; }
		});
		RadioButton c5 = new RadioButton("cost", "$$$$$ ");
		c5.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) { cost = 5; }
		});

		costPanel.add(c1);
		costPanel.add(c2);
		costPanel.add(c3);
		costPanel.add(c4);
		costPanel.add(c5);
		costPanel.add(new HTML("<br>"));
		this.add(costPanel);
		
		/*
		 * Review TextArea
		 */
		this.add(new HTML("<h4>Your Review</h4><br>"));
		reviewTextArea.setText("Your review helps other Vancouverites "
				+ "learn about great food vendors around town. "
				+ "Please don't review this business if you are in "
				+ "any way associated with its owner or employees.");
		reviewTextArea.setWidth("600px");
		reviewTextArea.setHeight("225px");
		reviewTextArea.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reviewTextArea.selectAll();
			}});
		this.add(reviewTextArea);

		/*
		 * Submit Review Button
		 */
		Button submitButton = new Button("Submit Review");
		submitButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				reviewText = reviewTextArea.getText();
				reviewTextArea.setText("");
				submitReview();
			}
		});
		this.add(submitButton);
	}

	private void submitReview() {
		Review review = new Review(userEmail, quality, cost, reviewText);
		vendor.addReview(review);
		vendorService = GWT.create(VendorService.class);
		vendorService.setVendor(vendor, new AsyncCallback<Void>() {
			public void onSuccess(Void result) {
				vendorInfoPanel.removeAddReviewsPanel();
				vendorInfoPanel.removeViewReviewsPanel();
				vendorInfoPanel.createViewReviewsPanel();
				VendorListPanel.getInstance().searchVendor();
			}
			public void onFailure(Throwable e) {
				Window.alert("Failed to submit review.");
				e.printStackTrace();
			}
		});
	}
}