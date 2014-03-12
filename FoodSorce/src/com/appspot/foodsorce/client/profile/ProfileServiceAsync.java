package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getProfile(String userEmail, AsyncCallback<Profile> callback);
	void setProfile(String userEmail, String photoUrl, String gender,
			String headline, String favouriteFood, String hometown,
			String websiteUrl, AsyncCallback<Void> callback);
}
