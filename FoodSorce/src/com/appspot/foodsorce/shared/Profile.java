package com.appspot.foodsorce.shared;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;

@PersistenceCapable(identityType = IdentityType.APPLICATION)
public class Profile implements Serializable {

	private static final long serialVersionUID = 1134649851446999024L;

	// Key is an app-assigned string ID that is the user's email address
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	private String encodedKey;
	
	@Persistent
	@Extension(vendorName="datanucleus", key="gae.pk-name", value="true")
	private String userEmail;

	@Persistent(serialized="true")
	private HashMap<String, String> settings = new HashMap<String, String>();
	
	// Default no-arg constructor required for serialization
	public Profile() {
		this("");
	}
	
	public Profile(String userEmail) {
		this.userEmail = userEmail;
		settings.put("photoUrl", "images/unknown_user.jpeg");
		settings.put("Gender", "");
		settings.put("Headline", "");
		settings.put("Favourite Food", "");
		settings.put("Hometown", "");
		settings.put("Website", "");
	}

	public String getUserEmail() {
		return userEmail;
	}

	public HashMap<String, String> getSettings() {
		return settings;
	}

	public void setSettings(HashMap<String, String> settings) {
		this.settings = settings;
	}

}
