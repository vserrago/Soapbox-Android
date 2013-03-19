package com.example.soapbox;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import android.app.TabActivity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public class MainActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login);
		

		Intent intent = new Intent().setClass(this, NewUser.class);
		//TabSpec tabSpecNewUser = 
		// Android tab
//		Intent intentAndroid = new Intent().setClass(this, AndroidActivity.class);
//		TabSpec tabSpecAndroid = tabHost
//		  .newTabSpec("Android")
//		  .setIndicator("", resources.getDrawable(R.drawable.icon_android_config))
//		  .setContent(intentAndroid);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.main, menu);
		return true;
	}

}
