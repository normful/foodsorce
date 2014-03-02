package com.appspot.foodsorce.client;

import java.io.Serializable;

public class NotLoggedInException extends Exception implements Serializable {
	
	private static final long serialVersionUID = -7918389401023841374L;

	public NotLoggedInException() {
		super();
	}

	public NotLoggedInException(String message) {
		super(message);
	}
}
