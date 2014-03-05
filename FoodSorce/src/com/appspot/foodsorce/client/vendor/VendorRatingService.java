package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vendorRatings")
public interface VendorRatingService extends RemoteService {
	public double[] getAverageQualities(int[] ids) throws NotLoggedInException;
	public double[] getAverageCosts(int[] ids) throws NotLoggedInException;
}
