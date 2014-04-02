package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class UserEmail implements Serializable{
	
	private static final long serialVersionUID = -6041773712947733435L;

	// Key is a system-generated encoded String
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String key;
	
	// The email of this Review's author
	@Persistent
	private String email;
	
	// Default no-arg constructor required for serialization
	public UserEmail() {
		this("");
	}
	
	public UserEmail (String userEmail) {
		this.email = userEmail;
	}
	
	public void setUserEmail(String userEmail) {
		this.email = userEmail;
	}
	
	public String getUserEmail() {
		return email;
	}
	
	@Override
	public boolean equals(Object o)
	{
		UserEmail email = (UserEmail) o;
		return this.getUserEmail().equals(email.getUserEmail());
	}

}
