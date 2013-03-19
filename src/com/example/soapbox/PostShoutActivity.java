package com.example.soapbox;

import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class PostShoutActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_post_shout);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.post_shout, menu);
		return true;
	}

}
