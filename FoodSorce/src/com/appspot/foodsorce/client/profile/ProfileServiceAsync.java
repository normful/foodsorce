package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getAllProfiles(AsyncCallback<Profile[]> callback);
	void getProfile(String userEmail, AsyncCallback<Profile> callback);
	void updateProfile(String userEmail, Profile profile,
			AsyncCallback<Void> callback);
	public void deleteProfile(String userEmail, AsyncCallback<Void> callback) throws NotLoggedInException;
}
