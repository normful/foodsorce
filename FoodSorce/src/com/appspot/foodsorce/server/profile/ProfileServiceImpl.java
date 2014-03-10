package com.appspot.foodsorce.server.profile;

import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManagerFactory;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.shared.Profile;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements ProfileService {

	private static final long serialVersionUID = 8750335582866548785L;
	private static final Logger LOG = Logger.getLogger(ProfileServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
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
