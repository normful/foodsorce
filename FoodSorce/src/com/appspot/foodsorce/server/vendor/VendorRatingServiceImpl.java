package com.appspot.foodsorce.server.vendor;

import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.VendorRatingService;
import com.google.appengine.api.users.UserService;
import com.google.appengine.api.users.UserServiceFactory;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VendorRatingServiceImpl extends RemoteServiceServlet implements VendorRatingService {

	private static final long serialVersionUID = -7497509782005563882L;
	private static final Logger LOG = Logger.getLogger(VendorRatingServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public double[] getAverageQualities(Long[] ids) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.getPersistenceManager();
		double[] qualities = new double[ids.length];
		try {
			// TODO: Add code involving javax.jdo.Query to query existing Vendor objects from the datastore
		} finally {
			pm.close();
		}
		return qualities;
	}

	@Override
	public double[] getAverageCosts(Long[] ids) throws NotLoggedInException {
		checkLoggedIn();
		PersistenceManager pm = PMF.getPersistenceManager();
		double[] costs = new double[ids.length];
		try {
			// TODO: Add code involving javax.jdo.Query to query existing Vendor objects from the datastore
		} finally {
			pm.close();
		}
		return costs;
	}

	private void checkLoggedIn() throws NotLoggedInException {
		UserService userService = UserServiceFactory.getUserService();
		if (!userService.isUserLoggedIn())
			throw new NotLoggedInException("Not logged in.");
	}

}