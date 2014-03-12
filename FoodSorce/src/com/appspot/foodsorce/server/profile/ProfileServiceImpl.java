package com.appspot.foodsorce.server.profile;

import java.util.HashMap;

import javax.jdo.PersistenceManager;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.shared.Profile;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements
		ProfileService {

	private static final long serialVersionUID = 7494378034289081842L;

	@Override
	public Profile getProfile(String userEmail) throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();

//		Profile profile = null;
		Profile profile = new Profile(userEmail);
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			profile = pm.getObjectById(Profile.class, userEmail);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
//		if (profile == null)
//			profile = createProfile(userEmail);
		
		return profile;
	}

//	private Profile createProfile(String userEmail) {
//		Profile newProfile = new Profile(userEmail);
//		PersistenceManager pm = PMF.get().getPersistenceManager();
//		try {
//			pm.makePersistent(newProfile);
//		} catch (Throwable e) {
//			e.printStackTrace();
//		} finally {
//			pm.close();
//		}
//		return newProfile;
//	}

	@Override
	public void updateProfile(String userEmail, HashMap<String, String> newSettings)
			throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			Profile profile = pm.getObjectById(Profile.class, userEmail);
			profile.setSettings(newSettings);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
	}

	private void checkLoggedIn() throws NotLoggedInException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn())
			throw new NotLoggedInException("Not logged in.");
	}

}
