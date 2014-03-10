package com.appspot.foodsorce.client.admin;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;

@RemoteServiceRelativePath("vancouverData")
public interface VancouverDataService extends RemoteService {
	public void importData();
}
