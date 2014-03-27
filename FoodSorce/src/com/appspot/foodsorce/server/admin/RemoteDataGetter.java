package com.appspot.foodsorce.server.admin;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class RemoteDataGetter {
	URL toGet;
	public RemoteDataGetter(URL toGet){
		this.toGet=toGet;
	}
	
	
	public InputStream getData(){
	
		try {
			InputStream toRead = toGet.openStream();
			return toRead;
		} 
		catch (MalformedURLException e1) {
			System.out.print("Problem with URL format");
			e1.printStackTrace();
		} 
		catch (IOException e) {
			System.out.print("IOexception reading data file");
			e.printStackTrace();
		}
		
		return null;
	}

}
	

