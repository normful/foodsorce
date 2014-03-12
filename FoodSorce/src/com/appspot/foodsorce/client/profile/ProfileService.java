package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService {
	public Profile getProfile(String userEmail) throws NotLoggedInException;
	public void setProfile(String userEmail, String photoUrl, String gender,
			String headline, String favouriteFood, String hometown,
			String websiteUrl) throws NotLoggedInException;
}
