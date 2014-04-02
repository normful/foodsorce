package com.appspot.foodsorce.server.profile;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.ArrayList;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.shared.Profile;
import com.appspot.foodsorce.shared.SerieString;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements
		ProfileService {

	private static final long serialVersionUID = 4371227882020935692L;

	@Override
	public Profile getProfile(String userEmail) throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();
		Profile profile, detached = null;
		PersistenceManager pm = PMF.get().getPersistenceManager();
		pm.getFetchPlan().setGroup(FetchPlan.ALL);
		try {
			profile = pm.getObjectById(Profile.class, userEmail);
			detached = pm.detachCopy(profile);
		} catch (Throwable e) {
			detached = createProfile(userEmail);
		} finally {
			pm.close();
		}
		return detached;
	}
	
	@Override
	public Profile[] getAllProfiles() throws NotLoggedInException {	
		
		ArrayList<Profile> detachedProfiles = new ArrayList<Profile>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Profile.class);
 
		try {
			@SuppressWarnings("unchecked")
			List<Profile> results = (List<Profile>) q.execute();
			detachedProfiles.addAll(pm.detachCopyAll(results));
		} finally {
			q.closeAll();
			pm.close();
		}

		return (Profile[]) detachedProfiles.toArray(new Profile[0]);
	}

	private Profile createProfile(String userEmail) {
		// Create the new Profile object
		Profile newProfile = new Profile(userEmail);
		
		// Persist the new Profile object
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(newProfile);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		
		// Fetch and get a detached copy of the newly persisted Profile object
		pm = PMF.get().getPersistenceManager();
		Profile detachedNewProfile = null;
		try {
			pm.getFetchPlan().setGroup(FetchPlan.ALL);
			newProfile = pm.getObjectById(Profile.class, userEmail);
			detachedNewProfile = pm.detachCopy(newProfile);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
			
		return detachedNewProfile;
	}

	@Override
	public void updateProfile(String userEmail, Profile profile)
			throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(profile);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	

	@Override
	public void deleteProfile(Profile profile) throws NotLoggedInException {
		checkLoggedIn();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.deletePersistent(profile);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.flush();
			pm.close();
		}
	}

	private void checkLoggedIn() throws NotLoggedInException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn())
			throw new NotLoggedInException("Not logged in.");
	}

	@Override
	public SerieString getGraphUrl(String photoUrl) {
		SerieString graphUrl= new SerieString("images/unknown_user.jpeg");
		try {
			URL toLookup = new URL(photoUrl);
			HttpURLConnection connection = (HttpURLConnection) toLookup.openConnection();
			connection.setInstanceFollowRedirects(false);
			graphUrl.setText(connection.getHeaderField("Location"));
		} catch (Throwable e) {
			e.printStackTrace();
		}
		return graphUrl;
	}

}