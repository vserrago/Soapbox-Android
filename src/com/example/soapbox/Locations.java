package com.example.soapbox;

import java.util.HashMap;

public class Locations 
{
	public static String [] cityTags = 
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

	public static String [] cityNames = 
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
	
	public static HashMap<String,String> constructCityMap()
	{
		HashMap<String,String> cityMap = new HashMap<String, String>();
		
		for(int i=0; i< cityNames.length; i++)
		{
			cityMap.put(cityNames[i], cityTags[i]);
		}
		
		return cityMap;
	}
}