package com.appspot.foodsorce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorRatingServiceAsync {
	void getAverageCosts(int[] ids, AsyncCallback<double[]> callback);
	void getAverageQualities(int[] ids, AsyncCallback<double[]> callback);
}
