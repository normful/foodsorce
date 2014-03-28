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
import com.google.gwt.core.client.GWT;
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
			e.printStackTrace();
		} finally {
			pm.close();
		}

		if (detached == null)
			detached = createProfile(userEmail);
			
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
			profiles.addAll(results);
//			GWT.log(profiles.get(0).getUserEmail());
		} finally {
			q.closeAll();
			pm.close();
		}

		return (Profile[]) profiles.toArray(new Profile[0]);
		
//		Profile[] profiles = new Profile[4];
//		
//		
//		Profile p1 = new Profile("test1");
//		Profile p2 = new Profile("test2");
//		Profile p3 = new Profile("test3");
//		Profile p4 = new Profile("test4");
//		
//		profiles[0] = p1;
//		profiles[1] = p2;
//		profiles[2] = p3;
//		profiles[3] = p4;
//		
//		System.out.println("before");
//		
//		
//
//		return profiles;
	}

	private Profile createProfile(String userEmail) {
		Profile newProfile = new Profile(userEmail);
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(newProfile);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
		return newProfile;
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

	private void checkLoggedIn() throws NotLoggedInException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn())
			throw new NotLoggedInException("Not logged in.");
	}

}
