package com.example.soapbox;

public class Shout 
{
	public String author;
	public String message;
	public String timestamp;
	public int rating;

	public Shout(String author, String message, String timestamp, int rating)
	{
		this.author = author;
		this.message = message;
		this.timestamp = timestamp;
		this.rating = rating;
	}
}
