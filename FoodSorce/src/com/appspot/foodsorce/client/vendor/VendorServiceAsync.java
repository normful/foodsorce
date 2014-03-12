package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VendorServiceAsync {
	void getVendors(AsyncCallback<Vendor[]> callback);
	void setVendor(Vendor vendor, AsyncCallback<Void> callback);
}
