package com.appspot.foodsorce.client.vendor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorRatingServiceAsync {
	void getAverageCosts(Long[] ids, AsyncCallback<double[]> callback);
	void getAverageQualities(Long[] ids, AsyncCallback<double[]> callback);
}
