package com.appspot.foodsorce.client.profile;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService {
	public ProfileInfo getProfile() throws NotLoggedInException;
	public void setProfile(ProfileInfo profile) throws NotLoggedInException;
}
