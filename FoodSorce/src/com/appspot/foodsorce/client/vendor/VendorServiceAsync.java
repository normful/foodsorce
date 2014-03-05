package com.appspot.foodsorce.client.vendor;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void getVendorInfos(AsyncCallback<VendorInfo[]> callback);
}
