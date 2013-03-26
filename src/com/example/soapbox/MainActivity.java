package com.example.soapbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.auth.BasicScheme;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.protocol.HTTP;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

	public static final String HOSTNAME = "http://acx0.dyndns.org:3000/";
	public static final String SHOUTS = "shouts";
	public static final String SLASH = "/";
	
	SharedPreferences prefs;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}
	
	//called when Post button is clicked
	public void makePost(View view) {
		Intent intent = new Intent(this, PostShoutActivity.class);
		startActivity(intent);		
	}
	
	//called when Login button is clicked
	public void openLogin(View view) {
		Intent intent = new Intent(this, LoginActivity.class);
		startActivity(intent);
	}
	
	public void refreshShouts(View view)
	{
//		String url = HOSTNAME + SHOUTS;
//		String method = DisplayShoutListTask.POST;
//		
//		DisplayShoutListTask t = new DisplayShoutListTask(url,method,null);
//		try 
//		{
//			t.execute().get().toString();
//		} 
//		catch (InterruptedException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} 
//		catch (ExecutionException e) 
//		{
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}
}
