package com.example.soapbox;

import java.util.HashMap;

public class Locations 
{
	public static final String globalTag = "global";
	public static final String globalName = "All Locations";
	public static final String CHOOSESTATEMENT = "Choose a location";
	
	public static final String [] cityTags = 
		{
		"barrie",
		"calgary",
		"charlottetown",
		"edmonton",
		"fredrickton",
		"halifax",
		"hamilton",
		"iqualuit",
		"kitchener",
		"london",
		"montreal",
		"oshawa",
		"ottawa",
		"peterborough",
		"quebec_city",
		"regina",
		"sarnia",
		"saskatoon",
		"stcatharines",
		"stjohns",
		"toronto",
		"vancouver",
		"victoria",
		"whitehorse",
		"windsor",
		"winnipeg",
		"yellowknife"
		};

	public static final String [] cityNames = 
		{
		"Barrie, Ontario",
		"Calgary, Alberta",
		"Charlottetown, PEI",
		"Edmonton, Alberta",
		"Fredrickton, New Brunswick",
		"Halifax, Nova Scotia",
		"Hamilton, Ontario",
		"Iqualuit, Nunavut",
		"Kitchener, Ontario",
		"London, Ontario",
		"Montreal, Quebec",
		"Oshawa, Ontario",
		"Ottawa, Ontario",
		"Peterborough, Ontario",
		"Quebec City, Quebec",
		"Regina, Saskatchewan",
		"Sarnia, Ontario",
		"Saskatoon, Saskatchewan",
		"St. Catharines, Ontario",
		"St. Johns, Newfoundland",
		"Toronto, Ontario",
		"Vancouver, British Columbia",
		"Victoria, British Columbia",
		"Whitehorse, Yukon",
		"Windsor, Ontario",
		"Winnipeg, Manitoba",
		"Yellowknife, North West Territories"
		};
	
	//Names are the value
	public static final HashMap<String,String> nameValueMap = constructNameValueMap();
	//Tags are the value
	public static final HashMap<String,String> tagValueMap = constructTagValueMap();
	
	public static HashMap<String,String> constructNameValueMap()
	{
		HashMap<String,String> map = new HashMap<String, String>();
		for(int i=0; i< cityNames.length; i++)
		{
			map.put(cityTags[i],cityNames[i]);
		}
		return map;
	}
	
	public static HashMap<String,String> constructTagValueMap()
	{
		HashMap<String,String> map = new HashMap<String, String>();
		for(int i=0; i< cityNames.length; i++)
		{

			map.put(cityNames[i], cityTags[i]);
		}
		return map;
	}
}