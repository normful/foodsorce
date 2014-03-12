package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("review")
public interface ReviewService extends RemoteService {
	public Vendor addReview(Review rating, String vendorKey) throws NotLoggedInException;
}
