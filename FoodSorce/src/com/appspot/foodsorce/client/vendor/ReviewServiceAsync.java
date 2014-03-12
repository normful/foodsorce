package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Review;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ReviewServiceAsync {
	void addReview(Review rating, String vendorKey, AsyncCallback<Vendor> callback);
}
