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

	private static final long serialVersionUID = -765688761851924539L;
	
	@Override
	public Vendor[] getVendors() throws NotLoggedInException {
		ArrayList<Vendor> vendors = new ArrayList<Vendor>();
		
		PersistenceManager pm = PMF.get().getPersistenceManager();
		Query q = pm.newQuery(Vendor.class);
		q.setOrdering("name desc");

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
	
}