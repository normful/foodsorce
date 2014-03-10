package com.appspot.foodsorce.client.admin;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface VancouverDataServiceAsync {
	void importData(AsyncCallback<Void> callback);
}
