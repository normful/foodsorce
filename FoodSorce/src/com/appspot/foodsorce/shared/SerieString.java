package com.appspot.foodsorce.shared;

import java.io.Serializable;

import javax.jdo.annotations.Extension;
import javax.jdo.annotations.IdGeneratorStrategy;
import javax.jdo.annotations.IdentityType;
import javax.jdo.annotations.PersistenceCapable;
import javax.jdo.annotations.Persistent;
import javax.jdo.annotations.PrimaryKey;
@PersistenceCapable(identityType = IdentityType.APPLICATION, detachable = "true")
public class SerieString implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4864883399645618010L;
	
	@PrimaryKey
	@Persistent(valueStrategy = IdGeneratorStrategy.IDENTITY)
	@Extension(vendorName="datanucleus", key="gae.encoded-pk", value="true")
	String text;
	
	public SerieString(){
		this("");
	}
	
	public SerieString(String string){
		text = string;
	}
	public void setText(String string){
		text = string;
	}
	public String getText(){
		return text;
	}

}
