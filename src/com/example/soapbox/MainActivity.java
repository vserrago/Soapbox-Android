package com.example.soapbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.soapbox.DisplayShoutListTask.ShoutListCallbackInterface;
import com.example.soapbox.PostShoutTask.PostShoutCallbackInterface;
import com.example.soapbox.UpdateTask.UpdateCallbackInterface;


public class MainActivity extends Activity implements ShoutListCallbackInterface, UpdateCallbackInterface, PostShoutCallbackInterface
{
	public static final String HOSTNAME = "http://acx0.dyndns.org:3000/";
	public static final String SHOUTS = "shouts";
	public static final String USERS = "users";
	public static final String SLASH = "/";
	public static final int SHOUT_LENGTH = 140;
	public static final String RETURN_KEY= "returnedFromLoginScreen";

	SharedPreferences prefs;
	JSONArray shoutArray = null;
	String username = null;
	String location = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) 
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//Refresh shout list on MainActivity creation
		View v = (View)findViewById(R.layout.activity_main);
		refreshShouts(v);
	}

	@Override
	public void onResume()
	{
		super.onResume();
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		
		if (prefs.getBoolean(RETURN_KEY, false) == true)
		{
			View v = (View)findViewById(R.layout.activity_main);
			refreshShouts(v);
			prefs.edit().putBoolean(RETURN_KEY, false);
		}
		else
		{
			retrieveUserInfo();
		}
		this.invalidateOptionsMenu();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) 
	{
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) 
	{
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);

		Boolean loggedIn = prefs.getBoolean(LoginTask.LOGINSTATUSKEY, false);

		MenuItem usernameItem =  menu.findItem(R.id.main_menu_change_username);
		MenuItem locationItem =  menu.findItem(R.id.main_menu_change_location);
		MenuItem signOutItem = menu.findItem(R.id.main_menu_sign_out);
		MenuItem loginActionItem =  menu.findItem(R.id.main_menu_action_login);
		MenuItem postShoutActionItem =  menu.findItem(R.id.main_menu_action_post);
		if(!loggedIn)
		{
			usernameItem.setVisible(false);
			locationItem.setVisible(false);
			signOutItem.setVisible(false);
			postShoutActionItem.setVisible(false);
			loginActionItem.setVisible(true);
		}
		else
		{
			usernameItem.setVisible(true);
			locationItem.setVisible(true);
			signOutItem.setVisible(true);
			postShoutActionItem.setVisible(true);
			loginActionItem.setVisible(false);
		}
		return true;
	}

	//gets the user's info from sharedprefs
	public void retrieveUserInfo()
	{
		//Get the user's location from shared prefs if it is stored there
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);

		location = prefs.getString(LoginTask.TAG, LoginTask.DEFAULT_TAG_VALUE);
		username = prefs.getString(LoginTask.NAME, null);
		Boolean loggedIn = prefs.getBoolean(LoginTask.LOGINSTATUSKEY, false);

		TextView usernameLabel = (TextView) findViewById(R.id.username_label_register);
		if(!loggedIn)
		{
			usernameLabel.setText("Please sign in");	
			prefs.edit().putBoolean(LoginTask.LOGINSTATUSKEY, false).commit();
		}
		else
		{
			usernameLabel.setText("Welcome, " + username);
		}
		System.out.println("Location: " + location);
	}

	//called when Post button is clicked
	public void makePost(View view) 
	{
		//Intent intent = new Intent(this, PostShoutActivity.class);
		//startActivity(intent);
		prefs = this.getSharedPreferences(
			      "com.example.soapbox", Context.MODE_PRIVATE);
		final MainActivity context = this;
		final Dialog dialog = new Dialog(this);
		
		dialog.setContentView(R.layout.activity_post_shout);
		dialog.setTitle("Post Shout");
		
		Button cancelShout = (Button)dialog.findViewById(R.id.cancelShout);
		Button postShout = (Button)dialog.findViewById(R.id.postShout);
		final EditText shoutbox = (EditText)dialog.findViewById(R.id.shoutbox);
		
		cancelShout.setOnClickListener(new OnClickListener()
		{
				@Override
				public void onClick(View v) { dialog.dismiss(); }
		});
		
		postShout.setOnClickListener(new OnClickListener()
		{

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				String url ="http://acx0.dyndns.org:3000/shouts.json";
				String name = (prefs.getString(LoginTask.NAME, ""));
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				System.out.println(name);
				String message = shoutbox.getText().toString();
				
				if (message.isEmpty())
				{
					Toast.makeText(context, "Shout cannot be empty.", Toast.LENGTH_SHORT).show();
					return;
				} 
				else if (message.length() >= SHOUT_LENGTH)
				{
					Toast.makeText(context, "Comment too long", Toast.LENGTH_SHORT).show();
					return;
				}
				params.add(new BasicNameValuePair(PostShoutTask.AUTH, prefs.getString(LoginTask.AUTH, "")));
				params.add(new BasicNameValuePair(PostShoutTask.MESSAGE, message));
				params.add(new BasicNameValuePair(PostShoutTask.NAME, name));
				String tag = prefs.getString(LoginTask.TAG, LoginTask.DEFAULT_TAG_VALUE);
				System.out.println(tag);
				params.add(new BasicNameValuePair(PostShoutTask.TAG, tag));
				params.add(new BasicNameValuePair(PostShoutTask.NAME, name));
				
				
				PostShoutTask task = new PostShoutTask(url, PostShoutTask.POST, params, context, context);
				task.execute();
				dialog.dismiss();
				View v = (View)findViewById(R.layout.activity_main);
				context.refreshShouts(v);
			}
			
		});
		
		dialog.show();
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) 
	{
		View v = (View)findViewById(R.layout.activity_main);
		switch (item.getItemId()) 
		{
		case R.id.main_menu_change_username:
			changeUsername(v);
			break;
		case R.id.main_menu_change_location:
			changeLocation(v);
			break;
		case R.id.main_menu_sign_out:
			openLogin(v);
			break;
		case R.id.main_menu_action_refresh:
			refreshShouts(v);
			break;
		case R.id.main_menu_action_post:
			makePost(v);
			break;
		case R.id.main_menu_action_login:
			openLogin(v);
			break;
		default:
			return super.onOptionsItemSelected(item);
		}
		return true;
	}

	public void changeUsername(View view)
	{
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		final MainActivity context = this;
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.change_username);
		dialog.setTitle("Change Username");

		// set the custom dialog components - text, image and button
		final TextView text = (TextView) dialog.findViewById(R.id.change_username_textbox);

		Button dialogButton = (Button) dialog.findViewById(R.id.change_username_ok);
		// if button is clicked, close the custom dialog
		dialogButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				username = text.getText().toString();
				System.out.println(username);
				
				int id = prefs.getInt(LoginTask.ID, -1);
				
				String url = HOSTNAME + USERS + SLASH + id;
				String method = UpdateTask.PUT;
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				BasicNameValuePair tag = new BasicNameValuePair(LoginTask.NAME, username);
				params.add(tag);

				UpdateTask t = new UpdateTask(url, method, params, context, context);
				t.execute();

				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) dialog.findViewById(R.id.change_username_cancel);
		cancelButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {dialog.dismiss();}
		});

		dialog.show();
	}

	public void changeLocation(View view)
	{
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		final MainActivity context = this;
		// custom dialog
		final Dialog dialog = new Dialog(this);
		dialog.setContentView(R.layout.change_location);
		dialog.setTitle("Change Location");

		final HashMap<String,String> m = Locations.constructCityMap();

		final Spinner spinner = (Spinner)dialog.findViewById(R.id.change_location_spinner);
		ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, Locations.cityNames);
		spinner.setAdapter(spinnerArrayAdapter);

		Button okButton = (Button) dialog.findViewById(R.id.change_location_ok);
		okButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) 
			{
				String spinnerText = spinner.getSelectedItem().toString();
				location = m.get(spinnerText);
				System.out.println(location);
				
				int id = prefs.getInt(LoginTask.ID, -1);
				
				String url = HOSTNAME + USERS + SLASH + id;
				String method = UpdateTask.PUT;
				
				ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
				BasicNameValuePair tag = new BasicNameValuePair(LoginTask.TAG, location);
				params.add(tag);

				UpdateTask t = new UpdateTask(url, method, params, context, context);
				t.execute();
				
				dialog.dismiss();
			}
		});

		Button cancelButton = (Button) dialog.findViewById(R.id.change_location_cancel);
		cancelButton.setOnClickListener(new OnClickListener() 
		{
			@Override
			public void onClick(View v) {dialog.dismiss();}
		});

		dialog.show();
	}

	//called when Login button is clicked
	public void openLogin(View view) 
	{
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		Boolean loggedIn = prefs.getBoolean(LoginTask.LOGINSTATUSKEY, false);

		//If no user is logged in
		if(!loggedIn)
		{
			Intent intent = new Intent(this, LoginActivity.class);
			startActivity(intent);
			View v = (View)findViewById(R.layout.activity_main);
		}
		//Else user is logged in
		else
		{
			prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
			prefs.edit().clear().commit(); //Delete all sharedprefs

			prefs.edit().putBoolean(LoginTask.LOGINSTATUSKEY, false).commit();

			retrieveUserInfo();
			this.invalidateOptionsMenu();	//Reset Action bar
			View v = (View)findViewById(R.layout.activity_main);
			refreshShouts(v);
		}
	}

	public void refreshShouts(View view)
	{
		String url = HOSTNAME + SHOUTS;
		String method = DisplayShoutListTask.GET;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();

		retrieveUserInfo(); //Get the latest values

		//If the location tag is not global
		if(!location.equals(LoginTask.DEFAULT_TAG_VALUE))
		{
			BasicNameValuePair tag = new BasicNameValuePair(LoginTask.TAG, location);
			params.add(tag);
		}

		System.out.println("Pre Execute");

		DisplayShoutListTask t = new DisplayShoutListTask(url,method,params,this,this);
		t.execute();
		System.out.println("Post Execute");
	}

	//called after shout list task is finished
	@Override
	public void onShoutRequestComplete(JSONArray result) 
	{
		System.out.println("Complete");
		System.out.println(result);
		shoutArray = result;

		LinkedList<HashMap<String, String>> list = new LinkedList<HashMap<String,String>>();
		try 
		{
			for(int i=0; i<shoutArray.length(); i++)
			{
				JSONObject o = shoutArray.getJSONObject(i);
				HashMap<String, String> map = new HashMap<String, String>();

				map.put(DisplayShoutListTask.ID, o.getString(DisplayShoutListTask.ID));
				map.put(DisplayShoutListTask.NAME, o.getString(DisplayShoutListTask.NAME));
				map.put(DisplayShoutListTask.TAG, o.getString(DisplayShoutListTask.TAG));
				map.put(DisplayShoutListTask.MESSAGE, o.getString(DisplayShoutListTask.MESSAGE));
				list.addFirst(map);
			}
		} 
		catch (JSONException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		final ListView listView = (ListView) findViewById(R.id.list);

		// get data from the table by the ListAdapter
		ListAdapter adapter = new com.example.soapbox.ListAdapter
				(this, list , R.layout.shout_list_component,
						new String[] {DisplayShoutListTask.MESSAGE},
						new int[] { R.id.message_component });

		listView.setAdapter(adapter);

		final MainActivity mainActivity = this;
		
		listView.setOnItemClickListener(new OnItemClickListener() 
		{
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) 
			{
				@SuppressWarnings("unchecked")
				HashMap<String, String> o = (HashMap<String, String>) listView.getItemAtPosition(position);	        		
				//Toast.makeText(MainActivity.this, "ID '" + o.get("id") + "' was clicked.", Toast.LENGTH_SHORT).show(); 
				Intent intent = new Intent(mainActivity, CommentActivity.class);
				intent.putExtra(DisplayShoutListTask.ID, o.get(DisplayShoutListTask.ID));
				intent.putExtra(CommentActivity.MAP,o);
				startActivity(intent);
			}
		});
	}

	//called after update task is finished
	@Override
	public void onUpdateComplete() 
	{
		//Store Location in sharedprefs
		prefs = this.getSharedPreferences("com.example.soapbox", Context.MODE_PRIVATE);
		prefs.edit().putString(LoginTask.TAG, location).commit();
		prefs.edit().putString(LoginTask.NAME, username).commit();
		retrieveUserInfo();
		View v = (View)findViewById(R.layout.activity_main);
		refreshShouts(v);
	}

	//called after post task is finished
	@Override
	public void onPostRequestComplete(JSONObject result) {
		// TODO Auto-generated method stub
		
	}
}