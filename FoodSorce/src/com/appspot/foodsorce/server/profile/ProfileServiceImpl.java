package com.appspot.foodsorce.server.profile;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.FetchPlan;
import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.profile.ProfileService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.shared.Profile;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class ProfileServiceImpl extends RemoteServiceServlet implements
		ProfileService {

	private static final long serialVersionUID = 2325771712463720356L;

	@Override
	public Profile getProfile(String userEmail) throws NotLoggedInException {
		checkLoggedIn();
		if (userEmail == null || userEmail.isEmpty())
			throw new NotLoggedInException();

		Profile profile, detached = null;
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.getFetchPlan().setGroup(FetchPlan.ALL);
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
		
		ArrayList<Profile> profiles = new ArrayList<Profile>();

		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Profile.class);
 
		try {
			@SuppressWarnings("unchecked")
			List<Profile> results = (List<Profile>) q.execute();
			profiles.addAll(pm.detachCopyAll(results));
		} finally {
			q.closeAll();
			pm.close();
		}

		return (Profile[]) profiles.toArray(new Profile[0]);
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
	public void deleteProfile(String userEmail) throws NotLoggedInException {
		checkLoggedIn();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		
		Profile profile;
		
		try {
			pm.getFetchPlan().setGroup(FetchPlan.ALL);
			profile = pm.getObjectById(Profile.class, userEmail);
			pm.deletePersistent(profile);
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
