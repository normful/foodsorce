package com.appspot.foodsorce.client.profile;

import java.util.HashMap;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.shared.Profile;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("profile")
public interface ProfileService extends RemoteService {
	public Profile getProfile(String userEmail) throws NotLoggedInException;
	public void updateProfile(String userEmail, HashMap<String, String> newSettings) throws NotLoggedInException;
}
