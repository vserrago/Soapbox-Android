package com.example.soapbox;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.HashMap;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.example.soapbox.DisplayShoutListTask.ShoutListCallbackInterface;

import android.os.Bundle;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

public class MainActivity extends Activity implements ShoutListCallbackInterface
{
	public static final String HOSTNAME = "http://acx0.dyndns.org:3000/";
	public static final String SHOUTS = "shouts";
	public static final String SLASH = "/";
	
	SharedPreferences prefs;
	JSONArray shoutArray = null;;
	
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
		String url = HOSTNAME + SHOUTS;
		String method = DisplayShoutListTask.GET;
		
		System.out.println("Pre Execute");
		
		DisplayShoutListTask t = new DisplayShoutListTask(url,method,null,this,this);
		t.execute();
		
		System.out.println("Post Execute");
	}

	@Override
	public void onRequestComplete(JSONArray result) 
	{
		System.out.println("Complete");
		System.out.println(result);
		shoutArray = result;
		
		ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try 
		{
			for(int i=0; i<shoutArray.length(); i++)
			{
				JSONObject o = shoutArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();
				
				map.put(DisplayShoutListTask.ID, o.getString(DisplayShoutListTask.ID));
				map.put(DisplayShoutListTask.NAME, o.getString(DisplayShoutListTask.NAME));
				map.put(DisplayShoutListTask.MESSAGE, o.getString(DisplayShoutListTask.MESSAGE));
				
				list.add(map);
			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		final ListView lv = (ListView) findViewById(R.id.list);
		
		ListAdapter adapter = new SimpleAdapter(this, list , R.layout.activity_main,
				new String[] {DisplayShoutListTask.MESSAGE},
				new int[] { R.id.username_label_register });

		
		lv.setAdapter(adapter);

		//final ListView lv = getListView();
		lv.setTextFilterEnabled(true);
//		lv.setOnItemClickListener(listener)
		lv.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
//				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) lv.getItemAtPosition(position);	        		
				Toast.makeText(MainActivity.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show(); 

			}
		});
	}
}