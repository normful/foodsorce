package com.appspot.foodsorce.server.vendor;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.jdo.JDOHelper;
import javax.jdo.PersistenceManager;
import javax.jdo.PersistenceManagerFactory;
import javax.jdo.Query;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.VendorInfo;
import com.appspot.foodsorce.client.vendor.VendorService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VendorServiceImpl extends RemoteServiceServlet implements VendorService {

	private static final long serialVersionUID = -765688761851924539L;
	private static final Logger LOG = Logger.getLogger(VendorServiceImpl.class.getName());
	private static final PersistenceManagerFactory PMF =
			JDOHelper.getPersistenceManagerFactory("transactions-optional");
	
	@Override
	public VendorInfo[] getVendorInfos() throws NotLoggedInException {
		PersistenceManager pm = PMF.getPersistenceManager();
		List<VendorInfo> vendorInfos = new ArrayList<VendorInfo>();
		try {
			// TODO: Add code involving javax.jdo.Query to query existing Vendor objects from the datastore
		} finally {
			pm.close();
		}
		return (VendorInfo[]) vendorInfos.toArray(new VendorInfo[0]);
	}
	
}