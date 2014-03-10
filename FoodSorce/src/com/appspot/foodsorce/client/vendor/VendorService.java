package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendor")
public interface VendorService extends RemoteService {
	public Vendor[] getVendors() throws NotLoggedInException;
}
