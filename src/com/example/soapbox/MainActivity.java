package com.example.soapbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;

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
import android.view.Menu;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends Activity {

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
//		Intent intent = new Intent(this, LoginActivity.class);
//		startActivity(intent);
//		new RetreiveFeedTask().execute(urlToRssFeed);
//		new HTTPTest().execute();
		//(String url, String method, ArrayList<NameValuePair> params)
		
		
//		String url ="http://acx0.dyndns.org:3000/api/v1/sessions";
//		String method = "POST";
//		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
//		BasicNameValuePair email = new BasicNameValuePair(HttpBackgroundTask.EMAILKEY,"jpanar@example.com");
//		BasicNameValuePair password = new BasicNameValuePair(HttpBackgroundTask.PASSWORDKEY,"verylongpassword");
//		params.add(email);
//		params.add(password);
		
		//http://localhost:3000/api/v1/registrations -d 
		//"{\"user\":{\"email\":\"user1@example.com\",\"name\":\"anotheruser\",\"password\":\"secret\",\"password_confirmation\":\"secret\"}}"
		
		String url ="http://acx0.dyndns.org:3000/api/v1/registrations";
		String method = HttpBackgroundTask.POST;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		BasicNameValuePair name = new BasicNameValuePair(HttpBackgroundTask.USERNAMEKEY,"jpanarj");
		BasicNameValuePair email = new BasicNameValuePair(HttpBackgroundTask.EMAILKEY,"jpanar@example.com");
		BasicNameValuePair password = new BasicNameValuePair(HttpBackgroundTask.PASSWORDKEY,"8characters");
		BasicNameValuePair passwordC = new BasicNameValuePair(HttpBackgroundTask.PASSWORD_CKEY,"8characters");
		params.add(name);
		params.add(email);
		params.add(password);
		params.add(passwordC);
		//if this pops up {"success":false,"info"... } pops up then email is already taken
		System.out.println(new HttpBackgroundTask(url, method, params).execute().toString());
	}
	

}
