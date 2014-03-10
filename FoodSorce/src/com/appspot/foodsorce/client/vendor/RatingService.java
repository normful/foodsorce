package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Rating;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("ratings")
public interface RatingService extends RemoteService {
	/**
	 * Adds a new rating to an existing Vendor in the datastore
	 * and retrieves the updated Vendor from the datastore.
	 * 
	 * @param rating    Rating to add
	 * @param vendorKey string from "key" column in new_food_vendor_locations.xls
	 * @return          updated Vendor with the newly added Rating
	 */
	public Vendor addRating(Rating rating, String vendorKey) throws NotLoggedInException;
}
