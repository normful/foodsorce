package com.appspot.foodsorce.client.vendor;

import com.appspot.foodsorce.client.Rating;
import com.appspot.foodsorce.client.Vendor;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface RatingServiceAsync {
	/**
	 * Adds a new rating to an existing Vendor in the datastore
	 * and retrieves the updated Vendor from the datastore.
	 * 
	 * @param rating    Rating to add
	 * @param vendorKey string from "key" column in new_food_vendor_locations.xls
	 * @param callback  callback object
	 */	
	void addRating(Rating rating, String vendorKey, AsyncCallback<Vendor> callback);
}
