package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendor")
public interface VendorService extends RemoteService {
	public VendorInfo[] getVendorInfos() throws NotLoggedInException;
}