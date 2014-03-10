package com.appspot.foodsorce.server.data;

import java.util.ArrayList;

public class Vendor {

	private String Name;
	private String Description;
	private String Location;
	private double Latitude;
	private double Longitude;
	private ArrayList<Rating> Ratings;
	
	public Vendor(String name, String description, String location, double latitude, double longitude){
		if(name.equals(""))
			Name=description;
		else
			Name=name;
		Description=description;
		Location=location;
		Latitude=latitude;
		Longitude=longitude;
		Ratings = new ArrayList<Rating>();
	}
	
	public String getName(){
		return Name;
	}
	
	public String getDescription(){
		return Description;
	}
	public String getLocation(){
		return Location;
	}
	
	public double getLatitude(){
		return Latitude;
	}
	
	public double getLongitude(){
		return Longitude;
	}
	
	public void addRating(Rating newRating){
		Ratings.add(newRating);
	}
	public ArrayList<Rating> getRatings(){
		return Ratings;
	}
//	public Rating getAvgRating(){
	//	av
		//return new Rating(0, 0,"","");
//	}
}
