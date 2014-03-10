package com.appspot.foodsorce.server.profile;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.shared.Profile;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

	private static final long serialVersionUID = 8750335582866548785L;
	
	@Override
	public Profile getProfile() throws NotLoggedInException {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setProfile(Profile profile) throws NotLoggedInException {
		// TODO Auto-generated method stub
	}
	
	private void createProfile() {
		// TODO
	}
	
	private void checkLoggedIn() throws NotLoggedInException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn())
			throw new NotLoggedInException("Not logged in.");
	}

}
