package com.appspot.foodsorce.server.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.ReviewService;
import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ReviewServiceImpl extends RemoteServiceServlet implements ReviewService {

	private static final long serialVersionUID = -7497509782005563882L;

	@Override
	public Vendor addReview(Review rating, String vendorKey)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

}