package com.appspot.foodsorce.client.profile;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getProfile(AsyncCallback<ProfileInfo> callback);
	void setProfile(ProfileInfo profile, AsyncCallback<Void> callback);
}
