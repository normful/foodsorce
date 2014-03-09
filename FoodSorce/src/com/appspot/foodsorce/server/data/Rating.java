package com.appspot.foodsorce.server.data;

public class Rating  {
	private int Quality;
	//between 0 and 10, corresponds to 0-5 half-stars
	
	private int Cost;
	//between 1 and 5, corresponds to 1-5 $
	
	private String Review;
	private String Username;
	//username of user that rated
	
	public Rating(int quality, int cost, String review, String user)throws IllegalArgumentException{
		if(quality<0||quality>10)
			throw new IllegalArgumentException("invalid quality value");
		else Quality=quality;
		if(cost<1||cost>5)
			throw new IllegalArgumentException("invalid cost value");
		else Cost=cost;
		Review=review;
		Username=user;
	}
	public int getQuality(){
		return Quality;
	}
	
	public int getCost(){
		return Cost;
	}
	
	public String getReview(){
		return Review;
	}
	
	public String getUsername(){
		return Username;
	}

}
