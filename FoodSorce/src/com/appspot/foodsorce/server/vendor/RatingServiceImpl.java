package com.appspot.foodsorce.server.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.RatingService;
import com.appspot.foodsorce.client.Rating;
import com.appspot.foodsorce.client.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class RatingServiceImpl extends RemoteServiceServlet implements RatingService {

	private static final long serialVersionUID = -7497509782005563882L;

	@Override
	public Vendor addRating(Rating rating, String vendorKey)
			throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

}