package com.example.soapbox;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ActionBar;
import android.app.AlertDialog;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import com.example.soapbox.LoginTask.MyCallbackInterface;

public class LoginActivity extends FragmentActivity implements
		ActionBar.TabListener, MyCallbackInterface {
	
	/**
	 * The {@link android.support.v4.view.PagerAdapter} that will provide
	 * fragments for each of the sections. We use a
	 * {@link android.support.v4.app.FragmentPagerAdapter} derivative, which
	 * will keep every loaded fragment in memory. If this becomes too memory
	 * intensive, it may be best to switch to a
	 * {@link android.support.v4.app.FragmentStatePagerAdapter}.
	 */
	public static final int NUM_TABS = 2;
	SectionsPagerAdapter mSectionsPagerAdapter;

	/**
	 * The {@link ViewPager} that will host the section contents.
	 */
	ViewPager mViewPager;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);

		// Set up the action bar.
		final ActionBar actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

		// Create the adapter that will return a fragment for each of the three
		// primary sections of the app.
		mSectionsPagerAdapter = new SectionsPagerAdapter(
				getSupportFragmentManager());

		// Set up the ViewPager with the sections adapter.
		mViewPager = (ViewPager) findViewById(R.id.pager);
		mViewPager.setAdapter(mSectionsPagerAdapter);

		// When swiping between different sections, select the corresponding
		// tab. We can also use ActionBar.Tab#select() to do this if we have
		// a reference to the Tab.
		mViewPager
				.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
					@Override
					public void onPageSelected(int position) {
						actionBar.setSelectedNavigationItem(position);
					}
				});

		// For each of the sections in the app, add a tab to the action bar.
		for (int i = 0; i < NUM_TABS; i++) {
			// Create a tab with text corresponding to the page title defined by
			// the adapter. Also specify this Activity object, which implements
			// the TabListener interface, as the callback (listener) for when
			// this tab is selected.
			actionBar.addTab(actionBar.newTab()
					.setText(mSectionsPagerAdapter.getPageTitle(i))
					.setTabListener(this));
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.login, menu);
		return true;
	}

	@Override
	public void onTabSelected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
		// When the given tab is selected, switch to the corresponding page in
		// the ViewPager.
		mViewPager.setCurrentItem(tab.getPosition());
	}

	@Override
	public void onTabUnselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}

	@Override
	public void onTabReselected(ActionBar.Tab tab,
			FragmentTransaction fragmentTransaction) {
	}
	
	public void login(View view)
	{
		String url ="http://acx0.dyndns.org:3000/api/v1/sessions";
		String method = "POST";
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		EditText editText = (EditText) findViewById(R.id.usernamelogin);
		BasicNameValuePair email = new BasicNameValuePair(LoginTask.EMAILKEY,editText.getText().toString());
		
		editText = (EditText) findViewById(R.id.passwordlogin);
		BasicNameValuePair password = new BasicNameValuePair(LoginTask.PASSWORDKEY,editText.getText().toString());
		params.add(email);
		params.add(password);
		
		LoginTask t = new LoginTask(url, method, params, this, this);
		t.execute();

		//TODO: if login success, finish, else, stay
	}

	public void register(View view)
	{
		String url ="http://acx0.dyndns.org:3000/api/v1/registrations";
		String method = LoginTask.POST;
		ArrayList<NameValuePair> params = new ArrayList<NameValuePair>();
		
		EditText editText = (EditText)findViewById(R.id.password1register);
		String password1 = editText.getText().toString();
		
		editText = (EditText)findViewById(R.id.password2register);
		String password2 = editText.getText().toString();
		
		if (!password1.equals(password2))
		{
			
			AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);
			
			dlgAlert.setTitle("Error registering");
			dlgAlert.setMessage("Passwords do not match.");
			dlgAlert.setPositiveButton("Ok",
				    new DialogInterface.OnClickListener() {
				        public void onClick(DialogInterface dialog, int which) {
				          //dismiss the dialog  
				        }
				    });
			dlgAlert.setCancelable(true);
			dlgAlert.create().show();
			return;
		}
		
		editText = (EditText) findViewById(R.id.usernameregister);
		BasicNameValuePair name = new BasicNameValuePair(LoginTask.USERNAMEKEY, editText.getText().toString());
		
		editText = (EditText)findViewById(R.id.emailregister);
		BasicNameValuePair email = new BasicNameValuePair(LoginTask.EMAILKEY, editText.getText().toString());
		
		BasicNameValuePair password = new BasicNameValuePair(LoginTask.PASSWORDKEY, password1);
		BasicNameValuePair passwordC = new BasicNameValuePair(LoginTask.PASSWORD_CKEY, password2);
		
		Spinner spinner = (Spinner)findViewById(R.id.register_location_spinner);
		String tag = Locations.tagValueMap.get(spinner.getSelectedItem().toString());
		
		BasicNameValuePair location = new BasicNameValuePair(LoginTask.TAGKEY, tag);
		
		params.add(name);
		params.add(email);
		params.add(password);
		params.add(passwordC);
		params.add(location);
		
		LoginTask t = new LoginTask(url, method, params, this, this);
		t.execute();

	}
	
	/**
	 * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
	 * one of the sections/tabs/pages.
	 */
	public class SectionsPagerAdapter extends FragmentPagerAdapter {

		public SectionsPagerAdapter(FragmentManager fm) {
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			// getItem is called to instantiate the fragment for the given page.
			// Return a DummySectionFragment (defined as a static inner class
			// below) with the page number as its lone argument.
			Fragment fragment = new DummySectionFragment();
			Bundle args = new Bundle();
			args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, position + 1);
			fragment.setArguments(args);
			return fragment;
		}


		@Override
		public CharSequence getPageTitle(int position) {
			Locale l = Locale.getDefault();
			switch (position) {
			case 0:
				return getString(R.string.login).toUpperCase(l);
			case 1:
				return getString(R.string.register).toUpperCase(l);
			}
			return null;
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return NUM_TABS;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply
	 * displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {
		/**
		 * The fragment argument representing the section number for this
		 * fragment.
		 */
		public static final String ARG_SECTION_NUMBER = "section_number";

		public DummySectionFragment() {
		}

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
				Bundle savedInstanceState) {
			View currentView = null;
			
			int section_num = getArguments().getInt(ARG_SECTION_NUMBER);
			if (section_num == 1) {				
				View login = inflater.inflate(R.layout.fragment_login, container, false);
				currentView = login;
			} else if (section_num == 2) {
				View register = inflater.inflate(R.layout.fragment_register, container, false);
				currentView = register;
				
//				View headerView = inflater.inflate(R.layout.fragment_register, container);

				final Spinner spinner = (Spinner)register.findViewById(R.id.register_location_spinner);
				ArrayAdapter<String> spinnerArrayAdapter = new ArrayAdapter<String>(register.getContext(), android.R.layout.simple_spinner_dropdown_item, Locations.cityNames);
				spinner.setAdapter(spinnerArrayAdapter);
			}
			
			return currentView;
		}
	}
	
	@Override
	public void onLoginRequestComplete(JSONObject result) 
	{
		// TODO Auto-generated method stuff
		SharedPreferences prefs = this.getSharedPreferences(
			      "com.example.soapbox", Context.MODE_PRIVATE);
		
		try 
		{
			System.out.println(result);
			
			//If registered
			if(result.getString(LoginTask.INFO).equals(LoginTask.INFOREGISTER))
			{
				prefs.edit().putString(LoginTask.EMAIL, result.getJSONObject(LoginTask.DATA).getJSONObject(LoginTask.USER).getString(LoginTask.EMAIL)).commit();
				prefs.edit().putString(LoginTask.NAME, result.getJSONObject(LoginTask.DATA).getJSONObject(LoginTask.USER).getString(LoginTask.NAME)).commit();
				prefs.edit().putInt(LoginTask.ID, result.getJSONObject(LoginTask.DATA).getJSONObject(LoginTask.USER).getInt(LoginTask.ID)).commit();
				prefs.edit().putString(LoginTask.TAG, result.getJSONObject(LoginTask.DATA).getJSONObject(LoginTask.USER).getString(LoginTask.TAG)).commit();
				prefs.edit().putString(LoginTask.AUTH, result.getJSONObject(LoginTask.DATA).getString(LoginTask.AUTH)).commit();
				
			}
			//else logged in
			else
			{
				EditText editText = (EditText) findViewById(R.id.usernamelogin);
				prefs.edit().putString(LoginTask.EMAIL, editText.getText().toString()).commit();
				prefs.edit().putInt(LoginTask.ID, result.getJSONObject(LoginTask.DATA).getInt(LoginTask.ID)).commit();
				prefs.edit().putString(LoginTask.TAG, result.getJSONObject(LoginTask.DATA).getString(LoginTask.TAG)).commit();
				prefs.edit().putString(LoginTask.NAME, result.getJSONObject(LoginTask.DATA).getString(LoginTask.USER)).commit();
				prefs.edit().putString(LoginTask.AUTH, result.getJSONObject(LoginTask.DATA).getString(LoginTask.AUTH)).commit();
			}
			
			prefs.edit().putBoolean(LoginTask.LOGINSTATUSKEY, true).commit();
			prefs.edit().putBoolean(MainActivity.RETURN_KEY, true).commit();
//			prefs.edit().putString(LoginTask.TAG, "toronto").commit();
			
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
	}
}
