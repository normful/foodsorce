package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void getVendors(AsyncCallback<Vendor[]> callback);
}
