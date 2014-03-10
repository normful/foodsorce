package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.client.Profile;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getProfile(AsyncCallback<Profile> callback);
	void setProfile(Profile profile, AsyncCallback<Void> callback);
}
