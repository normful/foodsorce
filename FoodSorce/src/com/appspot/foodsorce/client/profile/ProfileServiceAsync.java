package com.appspot.foodsorce.client.profile;

import java.util.HashMap;

import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getProfile(String userEmail, AsyncCallback<Profile> callback);
	void updateProfile(String userEmail,
			HashMap<String, String> newSettings,
			AsyncCallback<Void> callback);
}
