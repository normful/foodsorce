package com.appspot.foodsorce.server.profile;

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

	private static final long serialVersionUID = 8750335582866548785L;

	@Override
	public Profile getProfile(String userEmail) throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();

		Profile profile = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			profile = pm.getObjectById(Profile.class, userEmail);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}

		if (profile == null)
			return new Profile(userEmail);
		else
			return profile;
	}

	@Override
	public void setProfile(String userEmail, String photoUrl, String gender,
			String headline, String favouriteFood, String hometown,
			String websiteUrl) throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();

		PersistenceManager pm = PMF.get().getPersistenceManager();

		try {
			Profile profile = pm.getObjectById(Profile.class, userEmail);
			if (photoUrl != null && !photoUrl.isEmpty())
				profile.setPhotoUrl(photoUrl);
			if (gender != null && !gender.isEmpty())
				profile.setGender(gender);
			if (headline != null && !headline.isEmpty())
				profile.setHeadline(headline);
			if (favouriteFood != null && !favouriteFood.isEmpty())
				profile.setFavouriteFood(favouriteFood);
			if (hometown != null && !hometown.isEmpty())
				profile.setHometown(hometown);
			if (websiteUrl != null && !websiteUrl.isEmpty())
				profile.setWebsiteUrl(websiteUrl);
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
