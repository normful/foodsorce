package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.shared.Profile;
import com.appspot.foodsorce.shared.SerieString;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface ProfileServiceAsync {
	void getAllProfiles(AsyncCallback<Profile[]> callback);
	void getProfile(String userEmail, AsyncCallback<Profile> callback);
	void updateProfile(String userEmail, Profile profile, AsyncCallback<Void> callback);
	void deleteProfile(Profile profile, AsyncCallback<Void> callback);
	void getGraphUrl(String photoUrl, AsyncCallback<SerieString> callback);
}
