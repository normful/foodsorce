package com.appspot.foodsorce.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void getVendorInfos(AsyncCallback<VendorInfo[]> callback);
}
