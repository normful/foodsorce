package com.appspot.foodsorce.server.vendor;

import java.util.ArrayList;
import java.util.List;

import javax.jdo.PersistenceManager;
import javax.jdo.Query;

import com.appspot.foodsorce.client.login.NotLoggedInException;
import com.appspot.foodsorce.client.vendor.VendorService;
import com.appspot.foodsorce.server.PMF;
import com.appspot.foodsorce.shared.Vendor;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class VendorServiceImpl extends RemoteServiceServlet implements VendorService {
	

	/**
	 * 
	 */
	private static final long serialVersionUID = -5788857397262137930L;

	@Override
	public Vendor[] getVendors() throws NotLoggedInException {
		ArrayList<Vendor> vendors = new ArrayList<Vendor>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Vendor.class);
		q.setOrdering("name ascending");

		try {
			@SuppressWarnings("unchecked")
			List<Vendor> results = (List<Vendor>) q.execute();
			vendors.addAll(results);
		} finally {
			q.closeAll();
			pm.close();
		}
		
		return (Vendor[]) vendors.toArray(new Vendor[0]);
	}

	@Override
	public void setVendor(Vendor vendor) {
		PersistenceManager pm = PMF.get().getPersistenceManager();
		try {
			pm.makePersistent(vendor);
		} catch (Throwable e) {
			e.printStackTrace();
		} finally {
			pm.close();
		}
	}
	
}